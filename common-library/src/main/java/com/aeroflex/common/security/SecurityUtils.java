package com.aeroflex.common.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static UUID getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new AccessDeniedException("No authenticated user found");
        }

        return UUID.fromString(authentication.getName());
    }

    public static boolean isRoleMatch(String role) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {
            throw new AccessDeniedException("No authenticated user found");
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(auth ->
                        auth.getAuthority().equalsIgnoreCase("ROLE_" + role.toUpperCase()));
    }
}

