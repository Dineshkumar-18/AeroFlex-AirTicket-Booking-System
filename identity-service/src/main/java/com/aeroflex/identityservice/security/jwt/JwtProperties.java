package com.aeroflex.identityservice.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {

    private String secretKey;

    private long accessTokenExpiration;

    private long refreshTokenExpiration;

    private long mfaLoginTokenExpiration;

    private long mfaLoginSetupTokenExpiration;

}