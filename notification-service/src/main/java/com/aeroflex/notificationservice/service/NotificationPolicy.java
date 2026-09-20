package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationAction;

import java.util.List;

public interface NotificationPolicy {

    List<NotificationAction> getActions(
            String eventType
    );
}
