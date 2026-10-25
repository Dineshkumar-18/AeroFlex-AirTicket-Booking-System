package com.aeroflex.paymentservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.PaymentMethod;
import com.aeroflex.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_payment_booking", columnList = "bookingId"),
        @Index(name = "idx_payment_idempotency", columnList = "idempotencyKey", unique = true),
        @Index(name = "idx_payment_txn_ref", columnList = "transactionReference", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Column(nullable = false)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false, unique = true, length = 50)
    private String transactionReference;

    @Column(nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @Column(length = 50)
    @Builder.Default
    private String provider = "AeroPay";

    @Column(length = 100)
    private String paymentDetails; // e.g. "Card ending in 4242" or "user@upi"

    private String failureReason;

    private LocalDateTime paidAt;
}
