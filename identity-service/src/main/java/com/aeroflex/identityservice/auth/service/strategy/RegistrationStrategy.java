package com.aeroflex.identityservice.auth.service.strategy;


import com.aeroflex.identityservice.auth.enums.RegistrationType;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.entity.User;

public interface RegistrationStrategy {

    RegistrationType supports();

    void handle(
            User user,
            RegisterRequest request
    );
}
