package com.aeroflex.notificationservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(NotificationTemplateProperties.class)
public class NotificationConfiguration {
}
