package com.aeroflex.identityservice.mfa.dto;

public record MfaSetupResponse(
        String qrCode,
        String manualEntryKey
) {
}
