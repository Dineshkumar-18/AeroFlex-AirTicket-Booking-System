package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.model.NotificationPolicy;

import java.util.List;
import java.util.Optional;

public interface PolicyEngine {

    List<NotificationPolicy> getPolicies(
            String eventType,
            String eventVersion);

    NotificationPolicy getPolicy(String templateCode, String version,
                                                  NotificationChannel channel);
}
