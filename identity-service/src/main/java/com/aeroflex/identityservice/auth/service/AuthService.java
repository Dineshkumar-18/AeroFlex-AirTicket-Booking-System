package com.aeroflex.identityservice.auth.service;

import com.aeroflex.identityservice.auth.dto.request.ChangePasswordRequest;
import com.aeroflex.identityservice.auth.dto.request.ForgotPasswordRequest;
import com.aeroflex.identityservice.auth.dto.request.RefreshTokenRequest;
import com.aeroflex.identityservice.auth.dto.request.ResetPasswordRequest;
import com.aeroflex.identityservice.auth.dto.response.RefreshTokenResponse;
import com.aeroflex.identityservice.auth.enums.LoginContext;
import com.aeroflex.identityservice.user.dto.request.LoginRequest;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.dto.response.AuthResponse;
import com.aeroflex.identityservice.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthResponse login(
            LoginRequest request,
            LoginContext context
    );

    UserResponse getCurrentUser();

    AuthResponse refreshToken(
            RefreshTokenRequest request
    );

    void changePassword(
            ChangePasswordRequest request
    );

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    void logout(
           String refreshToken
    );
}
