package com.aeroflex.notificationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification.template")
public record NotificationTemplateProperties(
        String emailPrefix,
        String smsPrefix
) {}
