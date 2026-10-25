package com.aeroflex.flightservice.flight.dto;

import com.aeroflex.common.enums.FlightStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightStatusChangeRequest {

    @NotNull(message = "Flight status is required")
    private FlightStatus status;

    private String reason;
}
