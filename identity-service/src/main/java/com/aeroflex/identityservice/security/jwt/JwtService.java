package com.aeroflex.identityservice.security.jwt;

import com.aeroflex.identityservice.security.principal.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.UUID;

public interface JwtService {

    String generateAccessToken(UserPrincipal principal);

    String generateRefreshToken(UserPrincipal principal);

    String generateMfaSetupToken(UUID userId);

    String generateMfaLoginToken(UUID userId);

    String extractUsername(String token);

    UUID extractUserId(String token);

    List<String> extractRoles(String token);

    boolean isTokenValid(
            String token,
            UserDetails userDetails);

    boolean isTokenValid(String token);

    JwtTokenType extractTokenType(String token);

    boolean isMfaSetupTokenValid(String token);

    boolean isMfaLoginTokenValid(String token);

}