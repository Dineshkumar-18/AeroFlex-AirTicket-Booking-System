package com.aeroflex.flightservice.aircraft.dto;

import jakarta.validation.constraints.*;

public record AircraftTypeRequest(

        @NotBlank(message = "Model is required")
        @Size(max = 50, message = "Model must not exceed 50 characters")
        String model,

        @NotBlank(message = "Manufacturer is required")
        @Size(max = 50, message = "Manufacturer must not exceed 50 characters")
        String manufacturer,

        @Min(value = 0, message = "Economy capacity cannot be negative")
        int economyCapacity,

        @Min(value = 0, message = "Premium economy capacity cannot be negative")
        int premiumEconomyCapacity,

        @Min(value = 0, message = "Business capacity cannot be negative")
        int businessCapacity,

        @Min(value = 0, message = "First class capacity cannot be negative")
        int firstClassCapacity,

        @NotBlank(message = "Seating configuration is required")
        @Size(max = 20, message = "Seating configuration must not exceed 20 characters")
        String seatingConfig
) {
}
