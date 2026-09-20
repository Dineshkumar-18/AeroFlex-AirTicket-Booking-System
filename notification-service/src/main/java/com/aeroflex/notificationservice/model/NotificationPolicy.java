package com.aeroflex.notificationservice.model;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "notification_policy",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notification_policy",
                        columnNames = {
                                "event_type",
                                "event_version",
                                "channel"
                        }
                )
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "event_version", nullable = false)
    private String eventVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    private String subject;

    @Column(nullable = false)
    private String templateCode;

    @Column(nullable = false)
    private String templateVersion;

    @Column(nullable = false)
    private boolean enabled;

}
