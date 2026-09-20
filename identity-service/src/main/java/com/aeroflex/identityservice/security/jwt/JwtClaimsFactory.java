package com.aeroflex.identityservice.security.jwt;

import com.aeroflex.identityservice.security.principal.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtClaimsFactory {

    public Map<String, Object> create(UserPrincipal principal, JwtTokenType tokenType) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", principal.getId());
        claims.put("username", principal.getUsername());


        claims.put(
                "roles",
                principal.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        claims.put("email", principal.getEmail());
        claims.put("tokenType", tokenType.name());

        return claims;
    }

    public Map<String, Object> create(
            UUID userId,
            JwtTokenType tokenType) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", userId.toString());
        claims.put("tokenType", tokenType.name());

        return claims;
    }
}