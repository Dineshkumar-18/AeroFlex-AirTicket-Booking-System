package com.aeroflex.flightservice.aircraft.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AircraftRequest(

        @NotBlank(message = "Registration number is required")
        @Size(
                max = 20,
                message = "Registration number must not exceed 20 characters"
        )
        String registrationNumber,

        @NotNull(message = "Airline ID is required")
        UUID airlineId,

        @NotNull(message = "Aircraft type ID is required")
        UUID aircraftTypeId,

        @Size(
                max = 50,
                message = "Aircraft name must not exceed 50 characters"
        )
        String name
) {
}