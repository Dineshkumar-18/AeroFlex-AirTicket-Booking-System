package com.aeroflex.identityservice.mfa.service;

public interface MfaSecretEncryptionService {

    String encrypt(String plainText);

    String decrypt(String encryptedText);
}
