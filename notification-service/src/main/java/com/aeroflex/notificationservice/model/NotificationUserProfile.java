package com.aeroflex.notificationservice.model;

import com.aeroflex.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
        name = "notification_user_profile",
        indexes = {
                @Index(name = "idx_notification_user_profile_user_id",
                        columnList = "user_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUserProfile extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    private String username;

    private String email;

    private String mobile;

    private boolean emailVerified;

    private boolean smsVerified;

    @Column(name = "profile_version", nullable = false)
    private Long profileVersion;
}
