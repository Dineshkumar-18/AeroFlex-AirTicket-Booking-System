package com.aeroflex.flightscheduleservice.dto;

import com.aeroflex.common.enums.FlightStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class FlightScheduleRequest {

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotNull(message = "Flight ID is required")
    private UUID flightId;

    @NotBlank(message = "Airline code is required")
    private String airlineCode;

    @NotBlank(message = "Airline name is required")
    private String airlineName;

    @NotBlank(message = "Departure airport code is required")
    private String departureAirportCode;

    @NotBlank(message = "Arrival airport code is required")
    private String arrivalAirportCode;

    private String departureCity;
    private String arrivalCity;

    @NotNull(message = "Scheduled departure time is required")
    private LocalDateTime scheduledDepartureTime;

    @NotNull(message = "Scheduled arrival time is required")
    private LocalDateTime scheduledArrivalTime;

    private String aircraftModel;
    private UUID aircraftId;

    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;

    private String gate;
    private String terminal;

    @NotNull(message = "Economy price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Economy price must be greater than 0")
    private BigDecimal economyPrice;

    private BigDecimal premiumEconomyPrice;
    private BigDecimal businessPrice;
    private BigDecimal firstClassPrice;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    @Min(value = 0, message = "Available seats cannot be negative")
    private int availableSeats;
}
