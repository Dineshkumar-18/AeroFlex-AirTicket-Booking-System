package com.aeroflex.identityservice.auth.service;

import com.aeroflex.identityservice.auth.entity.RefreshToken;
import com.aeroflex.identityservice.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface RefreshTokenService {

    RefreshToken create(
            User user,
            String refreshToken
    );

    RefreshToken validate(
            String refreshToken
    );

    void revoke(
            RefreshToken refreshToken
    );

    RefreshToken findByRefreshToken(
            String refreshToken
    );

    String extractRefreshToken(HttpServletRequest request);

    void clearRefreshTokenCookie(HttpServletResponse response);

    void deleteAllByUserId(UUID userId);
}
