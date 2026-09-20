package com.aeroflex.identityservice.auth.service;

import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.identityservice.auth.entity.RefreshToken;
import com.aeroflex.identityservice.auth.repository.RefreshTokenRepository;
import com.aeroflex.identityservice.auth.service.RefreshTokenService;
import com.aeroflex.identityservice.auth.util.RefreshTokenHashUtil;
import com.aeroflex.identityservice.security.jwt.JwtProperties;
import com.aeroflex.identityservice.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl
        implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenHashUtil refreshTokenHashUtil;
    private final JwtProperties jwtProperties;

    @Override
    @Transactional
    public RefreshToken create(
            User user,
            String refreshToken) {

        String tokenHash =
                refreshTokenHashUtil.hash(refreshToken);

        RefreshToken entity = RefreshToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .expiresAt(
                        LocalDateTime.now()
                                .plusSeconds(
                                        jwtProperties.getRefreshTokenExpiration() / 1000
                                )
                )
                .revoked(false)
                .build();

        return refreshTokenRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public RefreshToken validate(
            String refreshToken) {

       RefreshToken storedToken = findByRefreshToken(refreshToken);

        if (storedToken.isRevoked()) {
            throw new ResourceNotFoundException(
                    "Refresh token has been revoked."
            );
        }

        if (storedToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new ResourceNotFoundException(
                    "Refresh token has expired."
            );
        }

        return storedToken;
    }

    @Override
    @Transactional
    public void revoke(
            RefreshToken refreshToken) {

        refreshToken.setRevoked(true);
        refreshToken.setRevokedAt(LocalDateTime.now());

        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByRefreshToken(String refreshToken) {
        String tokenHash =
                refreshTokenHashUtil.hash(refreshToken);

        return refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Refresh token not found."
                        ));
    }

    public String extractRefreshToken(
            HttpServletRequest request) {

        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie ->
                        "refreshToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void clearRefreshTokenCookie(HttpServletResponse response) {

        ResponseCookie cookie =
                ResponseCookie.from("refreshToken", "")
                        .httpOnly(true)
                        .secure(true)
                        .path("/api/account/auth")
                        .maxAge(Duration.ZERO)
                        .sameSite("None")
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

    @Override
    public void deleteAllByUserId(UUID userId) {
        refreshTokenRepository.deleteAllByUser_Id(userId);
    }
}
