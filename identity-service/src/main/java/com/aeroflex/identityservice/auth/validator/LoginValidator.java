package com.aeroflex.identityservice.auth.validator;

import com.aeroflex.common.enums.UserStatus;
import com.aeroflex.identityservice.user.entity.User;
import com.aeroflex.common.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class LoginValidator {

    public void validate(User user) {

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BadRequestException("User account is inactive.");
        }

//        if (!user.getEmailVerified()) {
//            throw new BadRequestException("Please verify your email.");
//        }

    }

}
