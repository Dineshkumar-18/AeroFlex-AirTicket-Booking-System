package com.aeroflex.seatservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seat_locks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatLock extends BaseEntity {

    @Column(nullable = false)
    private UUID scheduleId;

    @Column(nullable = false, length = 10)
    private String seatNumber;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 100)
    private String lockToken;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
