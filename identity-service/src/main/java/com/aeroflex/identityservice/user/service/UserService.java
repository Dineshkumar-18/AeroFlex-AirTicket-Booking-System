package com.aeroflex.identityservice.user.service;

import com.aeroflex.identityservice.user.dto.request.RegisterRequest;
import com.aeroflex.identityservice.user.dto.response.UserResponse;
import com.aeroflex.identityservice.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    User registerCustomer(RegisterRequest request);

    Optional<User> findByUsernameOrEmail(
            String usernameOrEmail);

    User getById(UUID id);

    void changePassword(UUID userId, String rawPassword);
}
