package com.aeroflex.notificationservice.dto;

import com.aeroflex.common.dto.AttachmentReference;
import com.aeroflex.notificationservice.enums.NotificationChannel;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record NotificationDeliveryEvent(
        UUID notificationId,
        String eventId,
        String userId,
        NotificationChannel channel,
        String templateCode,
        String templateVersion,
        Map<String, Object> templateData,
        List<AttachmentReference> attachments
) {
}
