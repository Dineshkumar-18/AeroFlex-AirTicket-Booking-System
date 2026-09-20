package com.aeroflex.identityservice.auth.dto.response;

import com.aeroflex.identityservice.user.dto.response.AuthResponse;
import lombok.Builder;

@Builder
public record RefreshTokenResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        Long expiresIn

) {
}
