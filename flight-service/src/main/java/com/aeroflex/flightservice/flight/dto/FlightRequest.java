package com.aeroflex.flightservice.flight.dto;

import com.aeroflex.common.enums.FlightStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightRequest {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Airline ID is required")
    private UUID airlineId;

    @NotNull(message = "Aircraft ID is required")
    private UUID aircraftId;

    @NotNull(message = "Departure airport ID is required")
    private UUID departureAirportId;

    @NotNull(message = "Arrival airport ID is required")
    private UUID arrivalAirportId;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private int durationMinutes;

    @Min(value = 1, message = "Distance must be at least 1 km")
    private int distanceKm;

    private FlightStatus status;

    @Builder.Default
    private boolean active = true;
}
