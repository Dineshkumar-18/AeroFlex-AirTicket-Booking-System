package com.aeroflex.identityservice.mfa.entity;

import com.aeroflex.common.entity.BaseAuditEntity;
import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.identityservice.mfa.enums.MfaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "user_mfa",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_mfa_user",
                columnNames = "user_id"
        )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMfa extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * AES-GCM encrypted TOTP secret.
     */
    @Column(name = "secret", nullable = false, length = 500)
    private String secret;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private MfaStatus status = MfaStatus.NOT_CONFIGURED;
}
