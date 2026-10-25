package com.aeroflex.paymentservice.dto;

import com.aeroflex.common.enums.PaymentMethod;
import com.aeroflex.common.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private UUID id;
    private UUID bookingId;
    private UUID userId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionReference;
    private String idempotencyKey;
    private String provider;
    private String paymentDetails;
    private String failureReason;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
