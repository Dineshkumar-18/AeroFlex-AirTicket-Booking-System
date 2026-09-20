package com.aeroflex.notificationservice.repository;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.model.NotificationDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationDeliveryRepository
        extends JpaRepository<NotificationDelivery, UUID> {

    Optional<NotificationDelivery> findByNotificationId(UUID notificationId);
    Optional<NotificationDelivery> findByNotificationIdAndChannel(
            UUID notificationId,
            NotificationChannel channel
    );
}
