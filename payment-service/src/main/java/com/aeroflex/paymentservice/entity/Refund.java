package com.aeroflex.paymentservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refunds", indexes = {
        @Index(name = "idx_refund_payment", columnList = "paymentId"),
        @Index(name = "idx_refund_booking", columnList = "bookingId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Refund extends BaseEntity {

    @Column(nullable = false)
    private UUID paymentId;

    @Column(nullable = false)
    private UUID bookingId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String currency = "USD";

    @Column(nullable = false, length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.SUCCESS;

    @Column(nullable = false, unique = true, length = 50)
    private String refundReference;

    private LocalDateTime refundedAt;
}
