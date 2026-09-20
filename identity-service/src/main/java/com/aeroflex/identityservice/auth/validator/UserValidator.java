package com.aeroflex.identityservice.auth.validator;

import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.identityservice.auth.service.RegistrationServiceImpl;
import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserValidator.class);

    public void validateRegistration(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already exists.");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username already exists.");
        }
    }
}
