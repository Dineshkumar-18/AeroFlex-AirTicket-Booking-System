package com.aeroflex.identityservice.security.principal;

import com.aeroflex.common.enums.UserStatus;
import com.aeroflex.identityservice.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPrincipalFactory {

    public UserPrincipal create(User user) {

        List<GrantedAuthority> authorities = user.getUserRoles()
                .stream()
                .map(userRole ->(GrantedAuthority) new SimpleGrantedAuthority(
                        "ROLE_" + userRole.getRole().getName().name()))
                .toList();

        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .enabled(user.getStatus() == UserStatus.ACTIVE)
                .authorities(authorities)
                .build();
    }
}
