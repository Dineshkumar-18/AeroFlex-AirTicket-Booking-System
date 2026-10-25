package com.aeroflex.seatservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatLayoutInitRequest {

    @NotBlank(message = "Aircraft model is required")
    private String aircraftModel; // e.g. "Boeing 777-300ER" or "Airbus A320neo"

    @DecimalMin(value = "0.0", inclusive = false, message = "Base price must be greater than 0")
    private BigDecimal baseEconomyPrice;
}
