package com.aeroflex.identityservice.auth.service.strategy;

import com.aeroflex.identityservice.auth.enums.RegistrationType;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationStrategy
        implements RegistrationStrategy {

    @Override
    public RegistrationType supports() {
        return RegistrationType.USER;
    }

    @Override
    public void handle(
            User user,
            RegisterRequest request
    ) {
        // No additional processing required.
    }
}
