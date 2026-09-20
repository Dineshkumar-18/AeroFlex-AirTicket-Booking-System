package com.aeroflex.notificationservice.model;

import com.aeroflex.notificationservice.enums.DeliveryStatus;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notification_delivery",uniqueConstraints = @UniqueConstraint(
        name = "uk_notification_delivery_channel",
        columnNames = {
                "notification_id",
                "channel"
        }
))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "notification_id", nullable = false)
    private UUID notificationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @Column(nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount;

    private String providerMessageId;

    private String errorCode;

    private String errorMessage;

    private Instant createdAt;

    private Instant sentAt;
}
