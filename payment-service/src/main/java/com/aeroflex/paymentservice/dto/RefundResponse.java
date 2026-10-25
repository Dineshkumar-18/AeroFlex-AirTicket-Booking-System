package com.aeroflex.paymentservice.dto;

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
public class RefundResponse {
    private UUID id;
    private UUID paymentId;
    private UUID bookingId;
    private BigDecimal amount;
    private String currency;
    private String reason;
    private PaymentStatus status;
    private String refundReference;
    private LocalDateTime refundedAt;
    private LocalDateTime createdAt;
}
