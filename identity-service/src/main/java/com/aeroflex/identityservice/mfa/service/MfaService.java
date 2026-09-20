package com.aeroflex.identityservice.mfa.service;


import com.aeroflex.identityservice.mfa.dto.MfaSetupResponse;
import com.aeroflex.identityservice.mfa.dto.MfaVerifyRequest;
import com.aeroflex.identityservice.user.dto.response.AuthResponse;

public interface MfaService {

    MfaSetupResponse setup(String setupToken);

    AuthResponse verifySetup(
            String setupToken,
            MfaVerifyRequest request);

    AuthResponse verifyLogin(
            String loginToken,
            MfaVerifyRequest request);
}
