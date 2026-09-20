package com.aeroflex.identityservice.auth.service;

import com.aeroflex.identityservice.auth.enums.RegistrationType;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;

public interface RegistrationService {

    void register(
            RegisterRequest request,
            RegistrationType registrationType
    );
}