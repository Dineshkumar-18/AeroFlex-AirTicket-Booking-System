package com.aeroflex.flightscheduleservice.dto;

import com.aeroflex.common.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightScheduleResponse {
    private UUID id;
    private String flightNumber;
    private UUID flightId;
    private String airlineCode;
    private String airlineName;
    private String departureAirportCode;
    private String arrivalAirportCode;
    private String departureCity;
    private String arrivalCity;
    private LocalDateTime scheduledDepartureTime;
    private LocalDateTime scheduledArrivalTime;
    private LocalDateTime actualDepartureTime;
    private LocalDateTime actualArrivalTime;
    private String aircraftModel;
    private UUID aircraftId;
    private FlightStatus status;
    private String gate;
    private String terminal;
    private BigDecimal economyPrice;
    private BigDecimal premiumEconomyPrice;
    private BigDecimal businessPrice;
    private BigDecimal firstClassPrice;
    private int totalSeats;
    private int availableSeats;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
