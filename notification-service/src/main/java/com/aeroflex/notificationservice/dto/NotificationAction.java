package com.aeroflex.notificationservice.dto;

import com.aeroflex.notificationservice.enums.NotificationChannel;

public record NotificationAction(
        NotificationChannel channel,
        String templateCode
) {
}
