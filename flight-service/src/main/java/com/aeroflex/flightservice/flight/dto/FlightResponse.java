package com.aeroflex.flightservice.flight.dto;

import com.aeroflex.common.enums.FlightStatus;
import com.aeroflex.flightservice.airline.dto.AirlineDto;
import com.aeroflex.flightservice.airport.dto.AirportResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponse {
    private UUID id;
    private UUID airlineId;
    private UUID aircraftId;
    private String flightNumber;
    private AirlineDto airline;
    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;
    private int durationMinutes;
    private int distanceKm;
    private FlightStatus status;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
