package com.aeroflex.identityservice.security.jwt;

import com.aeroflex.identityservice.auth.util.PasswordResetProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtProperties.class, PasswordResetProperties.class})
public class JwtConfiguration {
}
