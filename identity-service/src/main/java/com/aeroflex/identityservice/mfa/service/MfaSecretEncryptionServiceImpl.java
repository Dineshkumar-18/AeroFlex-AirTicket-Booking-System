package com.aeroflex.identityservice.mfa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MfaSecretEncryptionServiceImpl
        implements MfaSecretEncryptionService {

    private static final String AES = "AES";

    private static final String AES_GCM_NO_PADDING =
            "AES/GCM/NoPadding";

    private static final int GCM_TAG_LENGTH = 128;

    private static final int IV_LENGTH = 12;

    private final MfaEncryptionProperties properties;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String encrypt(String plainText) {

        try {
            byte[] iv = new byte[IV_LENGTH];

            secureRandom.nextBytes(iv);

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            properties.getKey(),
                            AES
                    );

            Cipher cipher =
                    Cipher.getInstance(AES_GCM_NO_PADDING);

            GCMParameterSpec gcmParameterSpec =
                    new GCMParameterSpec(
                            GCM_TAG_LENGTH,
                            iv
                    );

            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    secretKey,
                    gcmParameterSpec
            );

            byte[] encrypted =
                    cipher.doFinal(
                            plainText.getBytes(StandardCharsets.UTF_8)
                    );

            /*
             * Store IV + ciphertext together.
             */
            byte[] result =
                    new byte[iv.length + encrypted.length];

            System.arraycopy(
                    iv,
                    0,
                    result,
                    0,
                    iv.length
            );

            System.arraycopy(
                    encrypted,
                    0,
                    result,
                    iv.length,
                    encrypted.length
            );

            return Base64.getEncoder()
                    .encodeToString(result);

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Failed to encrypt MFA secret",
                    ex
            );
        }
    }

    @Override
    public String decrypt(String encryptedText) {

        try {

            byte[] combined =
                    Base64.getDecoder()
                            .decode(encryptedText);

            byte[] iv =
                    new byte[IV_LENGTH];

            byte[] encrypted =
                    new byte[combined.length - IV_LENGTH];

            System.arraycopy(
                    combined,
                    0,
                    iv,
                    0,
                    IV_LENGTH
            );

            System.arraycopy(
                    combined,
                    IV_LENGTH,
                    encrypted,
                    0,
                    encrypted.length
            );

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            properties.getKey(),
                            AES
                    );

            Cipher cipher =
                    Cipher.getInstance(AES_GCM_NO_PADDING);

            GCMParameterSpec gcmParameterSpec =
                    new GCMParameterSpec(
                            GCM_TAG_LENGTH,
                            iv
                    );

            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    gcmParameterSpec
            );

            byte[] decrypted =
                    cipher.doFinal(encrypted);

            return new String(
                    decrypted,
                    StandardCharsets.UTF_8
            );

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Failed to decrypt MFA secret",
                    ex
            );
        }
    }
}