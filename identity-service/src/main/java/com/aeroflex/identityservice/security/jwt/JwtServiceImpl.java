package com.aeroflex.identityservice.security.jwt;

import com.aeroflex.identityservice.security.principal.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final JwtClaimsFactory jwtClaimsFactory;

    @Override
    public String generateAccessToken(UserPrincipal principal) {
        return generateToken(principal, JwtTokenType.ACCESS);
    }

    @Override
    public String generateRefreshToken(UserPrincipal principal) {
        return generateToken(principal, JwtTokenType.REFRESH);
    }

    @Override
    public String generateMfaSetupToken(UUID userId) {
        return generateToken(userId, JwtTokenType.MFA_SETUP);
    }

    @Override
    public String generateMfaLoginToken(UUID userId) {
        return generateToken(userId, JwtTokenType.MFA_LOGIN);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public UUID extractUserId(String token) {
        String userId = extractAllClaims(token)
                .get("userId", String.class);

        return UUID.fromString(userId);
    }

    @Override
    public List<String> extractRoles(String token) {
        return extractAllClaims(token)
                .get("roles", List.class);
    }

    @Override
    public boolean isTokenValid(
            String token,
            UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    // =========================================================

    private String generateToken(UserPrincipal principal,
                                 JwtTokenType tokenType) {

        long expiration = getExpiration(tokenType);

        Date now = new Date();

        Date expiry =
                new Date(now.getTime() + expiration);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())   // jti
                .claims(jwtClaimsFactory.create(principal, tokenType))
                .subject(principal.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    private String generateToken(
            UUID userId,
            JwtTokenType tokenType) {

        long expiration = getExpiration(tokenType);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .claims(jwtClaimsFactory.create(userId, tokenType))
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    private <T> T extractClaim(String token,
                               Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {

        return extractClaim(token, Claims::getExpiration)
                .before(new Date());
    }

    private SecretKey getSigningKey() {

        byte[] key = Decoders.BASE64.decode(
                jwtProperties.getSecretKey());

        return Keys.hmacShaKeyFor(key);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public JwtTokenType extractTokenType(String token) {

        String tokenType = extractAllClaims(token)
                .get("tokenType", String.class);

        return JwtTokenType.valueOf(tokenType);
    }

    private long getExpiration(JwtTokenType tokenType) {

        return switch (tokenType) {
            case ACCESS ->
                    jwtProperties.getAccessTokenExpiration();

            case REFRESH ->
                    jwtProperties.getRefreshTokenExpiration();

            case MFA_SETUP ->
                    jwtProperties.getMfaLoginSetupTokenExpiration();

            case MFA_LOGIN ->
                    jwtProperties.getMfaLoginTokenExpiration();

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported token type: " + tokenType);
        };
    }

    @Override
    public boolean isMfaSetupTokenValid(String token) {
        return isTokenValidForType(token, JwtTokenType.MFA_SETUP);
    }

    @Override
    public boolean isMfaLoginTokenValid(String token) {
        return isTokenValidForType(token, JwtTokenType.MFA_LOGIN);
    }

    private boolean isTokenValidForType(
            String token,
            JwtTokenType expectedType) {

        try {
            if (!isTokenValid(token)) {
                return false;
            }

            return extractTokenType(token) == expectedType;

        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

}
