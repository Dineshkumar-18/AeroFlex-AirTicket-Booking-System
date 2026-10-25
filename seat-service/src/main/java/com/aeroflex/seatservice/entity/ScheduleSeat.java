package com.aeroflex.seatservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.SeatClass;
import com.aeroflex.common.enums.SeatStatus;
import com.aeroflex.common.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schedule_seats",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_schedule_seat", columnNames = {"scheduleId", "seatNumber"})
        },
        indexes = {
                @Index(name = "idx_schedule_status", columnList = "scheduleId, status"),
                @Index(name = "idx_seat_lock_expiry", columnList = "status, lockExpiresAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleSeat extends BaseEntity {

    @Column(nullable = false)
    private UUID scheduleId;

    @Column(nullable = false, length = 10)
    private String seatNumber; // e.g. "1A", "12C"

    @Column(nullable = false)
    private int seatRow;

    @Column(nullable = false, length = 2)
    private String seatColumn; // e.g. "A", "B", "C"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatClass seatClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatType seatType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SeatStatus status = SeatStatus.AVAILABLE;

    private UUID lockedByUserId;

    @Column(length = 100)
    private String lockToken;

    private LocalDateTime lockExpiresAt;

    private UUID bookingId;

    @Version
    private Long version;
}
