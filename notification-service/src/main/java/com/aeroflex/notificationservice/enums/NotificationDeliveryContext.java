package com.aeroflex.notificationservice.enums;

import java.util.UUID;

public record NotificationDeliveryContext(
        UUID notificationId,
        UUID eventId
) {
}
