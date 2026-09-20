package com.aeroflex.common.events.identity;

public record PasswordResetRequestedPayload(
        String email,
        String resetLink
) {
}
