package com.aeroflex.bookingservice.dto;

import com.aeroflex.common.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingPayRequest {

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String paymentDetails; // e.g. "4242-4242-4242-4242" or "user@upi"

    @NotBlank(message = "Idempotency key is required")
    private String idempotencyKey;
}
