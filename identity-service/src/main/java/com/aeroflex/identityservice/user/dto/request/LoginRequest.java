package com.aeroflex.identityservice.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank
        String usernameOrEmail,

        @NotBlank
        String password

) {
}
