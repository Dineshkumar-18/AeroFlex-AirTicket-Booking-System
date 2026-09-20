package com.aeroflex.identityservice.auth.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.identityservice.auth.dto.request.ChangePasswordRequest;
import com.aeroflex.identityservice.auth.dto.request.ForgotPasswordRequest;
import com.aeroflex.identityservice.auth.dto.request.RefreshTokenRequest;
import com.aeroflex.identityservice.auth.dto.request.ResetPasswordRequest;
import com.aeroflex.identityservice.auth.enums.LoginContext;
import com.aeroflex.identityservice.auth.enums.RegistrationType;
import com.aeroflex.identityservice.auth.service.AuthService;
import com.aeroflex.identityservice.auth.service.RefreshTokenService;
import com.aeroflex.identityservice.auth.service.RegistrationService;
import com.aeroflex.identityservice.user.dto.request.LoginRequest;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.dto.response.AuthResponse;
import com.aeroflex.identityservice.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RegistrationService registrationService;
    private final RefreshTokenService refreshTokenService;
    private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(
            AuthService authService,
            RegistrationService registrationService,
            RefreshTokenService refreshTokenService
    ) {
        this.authService = authService;
        this.registrationService = registrationService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        logger.debug("Received request to register with {}", request);

        registrationService.register(
                request,
                RegistrationType.USER
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User registered successfully",
                        null
                )
        );
    }

    @PostMapping("/flight-owner/register")
    public ResponseEntity<ApiResponse<Void>>
    registerFlightOwner(
            @Valid @RequestBody RegisterRequest request
    ) {

        logger.debug("Received request to register with {}", request);

        registrationService.register(
                request,
                RegistrationType.FLIGHT_OWNER
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Flight owner registered successfully",
                        null
                )
        );
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<AuthResponse>>
    loginUser(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(
                        request,
                        LoginContext.USER
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }

    @PostMapping("/flight-owner/login")
    public ResponseEntity<ApiResponse<AuthResponse>>
    loginFlightOwner(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(
                        request,
                        LoginContext.FLIGHT_OWNER
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<AuthResponse>>
    loginAdmin(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(
                        request,
                        LoginContext.ADMIN
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>>
    getCurrentUser() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Current user retrieved",
                        authService.getCurrentUser()
                )
        );
    }

//    @GetMapping("/login-state")
//    public ResponseEntity<ApiResponse<LoginStateResponse>>
//    getLoginState() {
//
//        return ResponseEntity.ok(
//                ApiResponse.success(
//                        "Login state retrieved",
//                        authService.getLoginState()
//                )
//        );
//    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>>
    refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Token refreshed",
                        authService.refreshToken(request)
                )
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>>
    changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {

        authService.changePassword(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Password changed successfully",
                        null
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            HttpServletRequest request,
            HttpServletResponse response) {

        String refreshToken = refreshTokenService.extractRefreshToken(request);

        authService.logout(refreshToken);

        refreshTokenService.clearRefreshTokenCookie(response);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Logged out successfully."
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "If an account exists for this email, " +
                                "a password reset link has been sent."
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Password has been reset successfully."
                )
        );
    }
}