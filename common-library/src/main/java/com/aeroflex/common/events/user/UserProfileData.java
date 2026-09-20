package com.aeroflex.common.events.user;

public record UserProfileData(
        String userId,
        String username,
        String email,
        String mobile,
        boolean emailVerified,
        boolean smsVerified,
        Long version
) {
}
