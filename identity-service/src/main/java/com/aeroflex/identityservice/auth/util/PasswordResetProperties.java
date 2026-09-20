package com.aeroflex.identityservice.auth.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "security.password-reset")
public class PasswordResetProperties {

    private Duration tokenExpiration = Duration.ofMinutes(30);

    private String frontendResetUrl;
}
