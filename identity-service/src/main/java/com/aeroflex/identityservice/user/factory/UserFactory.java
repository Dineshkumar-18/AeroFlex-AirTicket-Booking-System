package com.aeroflex.identityservice.user.factory;

import com.aeroflex.common.enums.UserStatus;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public User createCustomer(RegisterRequest request,
                               String encodedPassword) {

        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(encodedPassword)
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .mfaEnabled(false)
                .build();
    }
}
