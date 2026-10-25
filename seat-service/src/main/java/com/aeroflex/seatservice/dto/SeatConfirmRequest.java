package com.aeroflex.seatservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatConfirmRequest {

    @NotNull(message = "Schedule ID is required")
    private UUID scheduleId;

    @NotEmpty(message = "At least one seat number is required")
    private List<String> seatNumbers;

    @NotNull(message = "Booking ID is required")
    private UUID bookingId;

    private String lockToken;
    private UUID userId;
}
