package com.aeroflex.identityservice.mfa.service;

import com.aeroflex.common.exception.BusinessException;
import com.aeroflex.common.exception.ResourceNotFoundException;

import com.aeroflex.identityservice.mfa.dto.MfaSetupResponse;
import com.aeroflex.identityservice.mfa.dto.MfaVerifyRequest;
import com.aeroflex.identityservice.mfa.entity.UserMfa;
import com.aeroflex.identityservice.mfa.enums.MfaStatus;
import com.aeroflex.identityservice.mfa.repository.UserMfaRepository;
import com.aeroflex.identityservice.security.jwt.JwtProperties;
import com.aeroflex.identityservice.security.jwt.JwtService;
import com.aeroflex.identityservice.security.principal.UserPrincipal;
import com.aeroflex.identityservice.security.principal.UserPrincipalFactory;
import com.aeroflex.identityservice.user.dto.response.AuthResponse;
import com.aeroflex.identityservice.user.entity.User;
import com.aeroflex.identityservice.user.repository.UserRepository;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MfaServiceImpl implements MfaService {

    private final UserMfaRepository userMfaRepository;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final MfaSecretEncryptionService encryptionService;

    private final SecretGenerator secretGenerator;

    private final QrDataFactory qrDataFactory;

    private final QrGenerator qrGenerator;

    private final CodeVerifier codeVerifier;

    private final JwtProperties  jwtProperties;

    private final UserPrincipalFactory userPrincipalFactory;

    /*
     * =========================================================
     * MFA SETUP
     * =========================================================
     */

    @Override
    @Transactional
    public MfaSetupResponse setup(String setupToken) {

        /*
         * 1. Validate MFA setup token.
         */
        if (!jwtService.isMfaSetupTokenValid(setupToken)) {

            throw new BusinessException(
                    "Invalid or expired MFA setup token"
            );
        }

        /*
         * 2. Extract userId from MFA token.
         *
         * No SecurityContext here because the user
         * does not have a normal access token yet.
         */
        UUID userId =
                jwtService.extractUserId(setupToken);

        /*
         * 3. Load user.
         */
        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        /*
         * 4. Find existing MFA record.
         */
        UserMfa userMfa =
                userMfaRepository.findByUserId(userId)
                        .orElse(null);

        /*
         * 5. Prevent setup if MFA is already enabled.
         */
        if (userMfa != null &&
                userMfa.getStatus() == MfaStatus.ENABLED) {

            throw new BusinessException(
                    "MFA is already enabled"
            );
        }

        /*
         * 6. Generate new TOTP secret.
         */
        String secret =
                secretGenerator.generate();

        /*
         * 7. Encrypt secret before storing.
         */
        String encryptedSecret =
                encryptionService.encrypt(secret);

        /*
         * 8. Create or update MFA record.
         */
        if (userMfa == null) {

            userMfa = UserMfa.builder()
                    .userId(userId)
                    .secret(encryptedSecret)
                    .status(MfaStatus.NOT_CONFIGURED)
                    .build();

        } else {

            userMfa.setSecret(encryptedSecret);

            userMfa.setStatus(
                    MfaStatus.NOT_CONFIGURED
            );
        }

        userMfaRepository.save(userMfa);

        /*
         * 9. Create QR data.
         */
        QrData qrData =
                qrDataFactory.newBuilder()
                        .label(user.getEmail())
                        .secret(secret)
                        .issuer("AeroFlex")
                        .build();

        /*
         * 10. Generate QR URI.
         */
        byte[] imageData = null;
        try {
            imageData = qrGenerator.generate(qrData);
        } catch (QrGenerationException e) {
            throw new RuntimeException(e);
        }

        String qrCode = Base64.getEncoder()
                .encodeToString(imageData);

        /*
         * 11. Return QR + manual key.
         *
         * The secret is returned ONLY during setup.
         */
        return new MfaSetupResponse(
                qrCode,
                secret
        );
    }

    /*
     * =========================================================
     * VERIFY MFA SETUP
     * =========================================================
     */

    @Override
    @Transactional
    public AuthResponse verifySetup(
            String setupToken,
            MfaVerifyRequest request) {

        /*
         * 1. Validate setup token.
         */
        if (!jwtService.isMfaSetupTokenValid(setupToken)) {

            throw new BusinessException(
                    "Invalid or expired MFA setup token"
            );
        }

        /*
         * 2. Extract userId.
         */
        UUID userId =
                jwtService.extractUserId(setupToken);

        /*
         * 3. Load MFA record.
         */
        UserMfa userMfa =
                userMfaRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "MFA configuration not found"
                                )
                        );

        /*
         * 4. Make sure MFA hasn't already been enabled.
         */
        if (userMfa.getStatus() ==
                MfaStatus.ENABLED) {

            throw new BusinessException(
                    "MFA is already enabled"
            );
        }

        /*
         * 5. Decrypt TOTP secret.
         */
        String secret =
                encryptionService.decrypt(
                        userMfa.getSecret()
                );

        /*
         * 6. Verify 6-digit TOTP code.
         */
        boolean valid =
                codeVerifier.isValidCode(
                        secret,
                        request.code()
                );

        if (!valid) {

            throw new BusinessException(
                    "Invalid MFA verification code"
            );
        }

        /*
         * 7. Enable MFA.
         */
        userMfa.setStatus(
                MfaStatus.ENABLED
        );

        userMfaRepository.save(userMfa);

        /*
         * 8. Load user.
         */
        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        /*
         * 9. Create UserPrincipal ONLY NOW.
         *
         * This is after successful MFA verification.
         */
        UserPrincipal principal =
               userPrincipalFactory.create(user);

        /*
         * 10. Generate normal authentication tokens.
         */
        return createAuthResponse(principal);
    }

    /*
     * =========================================================
     * VERIFY MFA LOGIN
     * =========================================================
     */

    @Override
    @Transactional(readOnly = true)
    public AuthResponse verifyLogin(
            String loginToken,
            MfaVerifyRequest request) {

        /*
         * 1. Validate MFA login token.
         */
        if (!jwtService.isMfaLoginTokenValid(loginToken)) {

            throw new BusinessException(
                    "Invalid or expired MFA login token"
            );
        }

        /*
         * 2. Extract userId.
         */
        UUID userId =
                jwtService.extractUserId(loginToken);

        /*
         * 3. Load MFA configuration.
         */
        UserMfa userMfa =
                userMfaRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "MFA configuration not found"
                                )
                        );

        /*
         * 4. MFA must be enabled.
         */
        if (userMfa.getStatus() !=
                MfaStatus.ENABLED) {

            throw new BusinessException(
                    "MFA is not enabled"
            );
        }

        /*
         * 5. Decrypt secret.
         */
        String secret =
                encryptionService.decrypt(
                        userMfa.getSecret()
                );

        /*
         * 6. Verify TOTP code.
         */
        boolean valid =
                codeVerifier.isValidCode(
                        secret,
                        request.code()
                );

        if (!valid) {

            throw new BusinessException(
                    "Invalid MFA verification code"
            );
        }

        /*
         * 7. Load user.
         */
        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        /*
         * 8. Create UserPrincipal only after MFA.
         */
        UserPrincipal principal =
                userPrincipalFactory.create(user);

        /*
         * 9. Generate normal access + refresh tokens.
         */
        return createAuthResponse(principal);
    }

    /*
     * =========================================================
     * CREATE NORMAL AUTH RESPONSE
     * =========================================================
     */

    private AuthResponse createAuthResponse(
            UserPrincipal principal) {

        String accessToken =
                jwtService.generateAccessToken(principal);

        String refreshToken =
                jwtService.generateRefreshToken(principal);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                // use your existing expiration value
                jwtProperties.getAccessTokenExpiration()
        );
    }
}
