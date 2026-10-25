package com.aeroflex.flightservice.aircraft.dto;

import java.util.UUID;

public record AircraftTypeResponse(
        UUID id,
        String model,
        String manufacturer,
        int totalCapacity,
        int economyCapacity,
        int premiumEconomyCapacity,
        int businessCapacity,
        int firstClassCapacity,
        String seatingConfig,
        boolean active
) {
}
