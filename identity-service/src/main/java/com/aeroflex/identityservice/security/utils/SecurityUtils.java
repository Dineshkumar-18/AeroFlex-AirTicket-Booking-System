package com.aeroflex.identityservice.security.utils;

import com.aeroflex.common.exception.UnauthorizedException;
import com.aeroflex.identityservice.security.principal.UserPrincipal;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    public static UserPrincipal getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof UserPrincipal principal)) {

            throw new UnauthorizedException("User is not authenticated.");
        }

        return principal;
    }

    public static UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return getCurrentUser().getAuthorities();
    }

    public static boolean hasRole(String role) {

        return getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority ->
                        authority.equals("ROLE_" + role));
    }

}
