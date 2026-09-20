package com.aeroflex.common.events.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

public record BookingConfirmedPayload(
        String bookingId,
        String pnr,
        String flightNumber,
        String origin,
        String destination,
        Instant departureTime,
        String seat,
        String fareClass,
        BigDecimal amountPaid,
        String currency
) {
}
