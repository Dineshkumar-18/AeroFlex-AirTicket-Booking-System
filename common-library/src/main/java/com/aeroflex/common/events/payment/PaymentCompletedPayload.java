package com.aeroflex.common.events.payment;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentCompletedPayload(
        String paymentId,
        String bookingId,
        String userId,
        BigDecimal amount,
        String currency,
        String paymentMethod,
        String transactionReference,
        Instant paidAt
) {
}
