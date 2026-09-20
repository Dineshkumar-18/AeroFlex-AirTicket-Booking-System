package com.aeroflex.identityservice.mfa.service;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Getter
public class MfaEncryptionProperties {

    private final byte[] key;

    public MfaEncryptionProperties(
            @Value("${security.mfa.encryption-key}") String encodedKey) {

        this.key = Base64.getDecoder()
                .decode(encodedKey);

        if (key.length != 32) {
            throw new IllegalArgumentException(
                    "MFA encryption key must be 256 bits (32 bytes)"
            );
        }
    }
}
