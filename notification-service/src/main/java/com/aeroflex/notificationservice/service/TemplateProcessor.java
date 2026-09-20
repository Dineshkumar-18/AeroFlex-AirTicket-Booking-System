package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;

import java.util.Map;

public interface TemplateProcessor {

    NotificationChannel channel();

    String process(
            String templateCode,
            String templateVersion,
            Map<String, Object> variables
    );
}
