package com.aeroflex.identityservice.auth.validator;

import com.aeroflex.identityservice.auth.dto.request.ChangePasswordRequest;
import com.aeroflex.common.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public void validate(ChangePasswordRequest request) {

        if (request.currentPassword().equals(request.newPassword())) {
            throw new BadRequestException(
                    "New password must be different from current password.");
        }
    }
}
