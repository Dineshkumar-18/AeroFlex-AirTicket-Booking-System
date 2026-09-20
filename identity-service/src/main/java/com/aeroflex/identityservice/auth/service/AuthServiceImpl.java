package com.aeroflex.identityservice.auth.service;

import com.aeroflex.common.constants.TopicConstants;
import com.aeroflex.common.enums.RoleType;
import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.common.events.KafkaEventPublisher;
import com.aeroflex.common.events.identity.PasswordResetRequestedPayload;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.common.security.SecurityUtils;
import com.aeroflex.identityservice.auth.dto.request.ChangePasswordRequest;
import com.aeroflex.identityservice.auth.dto.request.ForgotPasswordRequest;
import com.aeroflex.identityservice.auth.dto.request.RefreshTokenRequest;
import com.aeroflex.identityservice.auth.dto.request.ResetPasswordRequest;
import com.aeroflex.identityservice.auth.entity.PasswordResetToken;
import com.aeroflex.identityservice.auth.entity.RefreshToken;
import com.aeroflex.identityservice.auth.enums.LoginContext;
import com.aeroflex.identityservice.auth.repository.PasswordResetTokenRepository;
import com.aeroflex.identityservice.auth.util.PasswordResetProperties;
import com.aeroflex.identityservice.auth.util.PasswordResetTokenGenerator;
import com.aeroflex.identityservice.auth.validator.LoginValidator;
import com.aeroflex.identityservice.auth.validator.PasswordValidator;
import com.aeroflex.identityservice.exception.InvalidPasswordResetTokenException;
import com.aeroflex.identityservice.security.jwt.JwtProperties;
import com.aeroflex.identityservice.security.jwt.JwtService;
import com.aeroflex.identityservice.security.jwt.JwtTokenType;
import com.aeroflex.identityservice.security.password.PasswordService;
import com.aeroflex.identityservice.security.principal.UserPrincipal;
import com.aeroflex.identityservice.security.principal.UserPrincipalFactory;
import com.aeroflex.identityservice.user.dto.request.LoginRequest;
import com.aeroflex.identityservice.user.dto.response.AuthResponse;
import com.aeroflex.identityservice.user.dto.response.UserResponse;
import com.aeroflex.identityservice.user.entity.User;
import com.aeroflex.identityservice.user.mapper.UserMapper;
import com.aeroflex.identityservice.user.repository.FlightOwnerProfileRepository;
import com.aeroflex.identityservice.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FlightOwnerProfileRepository profileRepository;
    private final LoginValidator loginValidator;
    private final UserMapper userMapper;
    private final UserPrincipalFactory userPrincipalFactory;
    private final JwtProperties jwtProperties;
    private final PasswordValidator passwordValidator;
    private final PasswordService passwordService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenGenerator  tokenGenerator;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetProperties passwordResetProperties;
    private final KafkaEventPublisher kafkaEventPublisher;

    @Override
    @Transactional
    public AuthResponse login(
            LoginRequest request,
            LoginContext context
    ) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.usernameOrEmail(),
                                request.password()
                        ));

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        User user = findUser(request);

        loginValidator.validate(user);

        validateLoginContext(
                user,
                context
        );

        validateMfaIfRequired(
                user,
                context
        );

        String accessToken =
                jwtService.generateAccessToken(principal);

        String refreshToken =
                jwtService.generateRefreshToken(principal);

        refreshTokenService.create(
                user,
                refreshToken
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(
                        jwtProperties.getAccessTokenExpiration()
                )
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {

        UUID id = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        return userMapper.toResponse(user);

    }

    @Override
    @Transactional
    public AuthResponse refreshToken(
            RefreshTokenRequest request) {

        String refreshToken = request.refreshToken();

        // 1. Validate JWT signature + expiration
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new BadRequestException(
                    "Invalid refresh token."
            );
        }

        // 2. Validate token type
        if (jwtService.extractTokenType(refreshToken)
                != JwtTokenType.REFRESH) {

            throw new BadRequestException(
                    "Invalid token type."
            );
        }

        // 3. Validate against database
        RefreshToken storedToken =
                refreshTokenService.validate(refreshToken);

        // 4. Extract username
        String username =
                jwtService.extractUsername(refreshToken);

        // 5. Load latest user
        User user =
                userRepository
                        .findByEmailOrUsername(
                                username,
                                username
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found."
                                ));

        // 6. Ensure token belongs to this user
        if (!storedToken.getUser()
                .getId()
                .equals(user.getId())) {

            throw new BadRequestException(
                    "Refresh token does not belong to user."
            );
        }

        // 7. Validate current user status
        loginValidator.validate(user);

        // 8. Create principal
        UserPrincipal principal =
                userPrincipalFactory.create(user);

        // 9. Generate new tokens
        String newAccessToken =
                jwtService.generateAccessToken(principal);

        String newRefreshToken =
                jwtService.generateRefreshToken(principal);

        // 10. Revoke old refresh token
        refreshTokenService.revoke(storedToken);

        // 11. Persist new refresh token
        RefreshToken newStoredToken =
                refreshTokenService.create(
                        user,
                        newRefreshToken
                );

        // 12. Link old token -> new token
        storedToken.setReplacedByTokenId(
                newStoredToken.getId()
        );

        // 13. Return response
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(
                        jwtProperties.getAccessTokenExpiration()
                )
                .build();
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        passwordValidator.validate(request);

        UUID userId = SecurityUtils.getCurrentUserId();

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if (!passwordService.matches(request.currentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password is incorrect.");
        }

        user.setPassword(passwordService.encode(request.newPassword()));

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {

        String email = request.email().trim().toLowerCase();

        Optional<User> userOptional =
                userRepository.findByEmail(email);

        /*
         * IMPORTANT:
         * Do not reveal whether the email exists.
         */
        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();

        /*
         * Remove previous reset tokens.
         */
        passwordResetTokenRepository.deleteAllByUserId(user.getId());

        /*
         * Generate raw token.
         */
        String rawToken = tokenGenerator.generateToken();

        /*
         * Store only hash.
         */
        String tokenHash = tokenGenerator.hash(rawToken);

        PasswordResetToken resetToken =
                PasswordResetToken.builder()
                        .userId(user.getId())
                        .tokenHash(tokenHash)
                        .expiresAt(
                                Instant.now().plus(
                                        passwordResetProperties
                                                .getTokenExpiration()
                                )
                        )
                        .build();

        passwordResetTokenRepository.save(resetToken);

        /*
         * Build frontend reset URL.
         */
        String resetLink =
                UriComponentsBuilder
                        .fromUriString(
                                passwordResetProperties
                                        .getFrontendResetUrl()
                        )
                        .queryParam("token", rawToken)
                        .build()
                        .toUriString();

        PasswordResetRequestedPayload payload =
                new PasswordResetRequestedPayload(
                        user.getEmail(),
                        resetLink
                );

        kafkaEventPublisher.publish(TopicConstants.BUSINESS_EVENT_TOPIC,
                BusinessEvent.builder()
                        .eventId(String.valueOf(UUID.randomUUID()))
                        .eventType(
                                "PASSWORD_RESET"
                        )
                        .eventVersion("1.0")
                        .occurredAt(Instant.now())
                        .source("identity-service")
                        .userId(String.valueOf(user.getId()))
                        .correlationId(UUID.randomUUID().toString())
                        .payload(payload)
                        .build()
        );
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        String tokenHash =
                tokenGenerator.hash(request.token());

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new InvalidPasswordResetTokenException(
                                        "Invalid or expired password reset token"
                                ));

        if (!resetToken.isValid()) {
            throw new InvalidPasswordResetTokenException(
                    "Invalid or expired password reset token"
            );
        }

        User user =
                userRepository.findById(resetToken.getUserId())
                        .orElseThrow(() ->
                                new InvalidPasswordResetTokenException(
                                        "Invalid password reset request"
                                ));

        /*
         * Change password.
         */
        user.setPassword(
                passwordEncoder.encode(
                        request.newPassword()
                )
        );

        userRepository.save(user);

        /*
         * Mark token as consumed.
         */
        resetToken.setUsedAt(Instant.now());

        passwordResetTokenRepository.save(resetToken);

        /*
         * Optional but strongly recommended:
         * revoke all existing refresh tokens here.
         */
        refreshTokenService.deleteAllByUserId(user.getId());

        kafkaEventPublisher.publish(
                TopicConstants.BUSINESS_EVENT_TOPIC,
                BusinessEvent.builder()
                        .eventId(UUID.randomUUID().toString())
                        .eventType("PASSWORD_RESET_SUCCESS")
                        .eventVersion("1.0")
                        .occurredAt(Instant.now())
                        .source("identity-service")
                        .userId(user.getId().toString())
                        .correlationId(UUID.randomUUID().toString())
                        .payload(Map.of(
                                "email", user.getEmail(),
                                "firstName", user.getFirstName()
                        ))
                        .build()
        );
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }

        RefreshToken byRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);

        refreshTokenService.revoke(byRefreshToken);

    }


    private User findUser(
            LoginRequest request
    ) {

        return userRepository
                .findByEmailOrUsername(
                        request.usernameOrEmail(),
                        request.usernameOrEmail()
                )
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid username/email or password"
                        )
                );
    }

    private void validateLoginContext(
            User user,
            LoginContext context
    ) {

        RoleType requiredRole =
                switch (context) {

                    case USER ->
                            RoleType.USER;

                    case FLIGHT_OWNER ->
                            RoleType.FLIGHT_OWNER;

                    case ADMIN ->
                            RoleType.ADMIN;
                };

        boolean hasRole =
                user.getUserRoles()
                        .stream()
                        .anyMatch(role ->
                                role.getRole().getName()
                                        == requiredRole
                        );

        if (!hasRole) {

            throw new AccessDeniedException(
                    "User is not authorized for this login"
            );
        }
    }


    private void validateMfaIfRequired(
            User user,
            LoginContext context
    ) {

        if ((context == LoginContext.FLIGHT_OWNER
                || context == LoginContext.ADMIN)
                && !user.getMfaEnabled()) {

            throw new BadRequestException(
                    "MFA is required for this account"
            );
        }
    }
}
