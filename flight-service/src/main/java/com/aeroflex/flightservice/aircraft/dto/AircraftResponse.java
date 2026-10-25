package com.aeroflex.flightservice.aircraft.dto;

import com.aeroflex.flightservice.aircraft.enums.AircraftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public record AircraftResponse(

        UUID id,

        String registrationNumber,

        UUID airlineId,

        UUID aircraftTypeId,

        String aircraftTypeModel,

        String manufacturer,

        int totalCapacity,

        String name,

        AircraftStatus status,

        boolean isActive
) {
}
