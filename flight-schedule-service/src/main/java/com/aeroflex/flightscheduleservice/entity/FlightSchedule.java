package com.aeroflex.flightscheduleservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flight_schedules", indexes = {
        @Index(name = "idx_schedule_route_time", columnList = "departureAirportCode, arrivalAirportCode, scheduledDepartureTime"),
        @Index(name = "idx_schedule_flight_number", columnList = "flightNumber")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightSchedule extends BaseEntity {

    @Column(nullable = false, length = 20)
    private String flightNumber; // e.g. "AI-101"

    @Column(nullable = false)
    private UUID flightId;

    @Column(nullable = false, length = 10)
    private String airlineCode; // e.g. "AI"

    @Column(nullable = false, length = 100)
    private String airlineName; // e.g. "Air India"

    @Column(nullable = false, length = 3)
    private String departureAirportCode; // e.g. "DEL"

    @Column(nullable = false, length = 3)
    private String arrivalAirportCode; // e.g. "JFK"

    @Column(length = 100)
    private String departureCity;

    @Column(length = 100)
    private String arrivalCity;

    @Column(nullable = false)
    private LocalDateTime scheduledDepartureTime;

    @Column(nullable = false)
    private LocalDateTime scheduledArrivalTime;

    private LocalDateTime actualDepartureTime;

    private LocalDateTime actualArrivalTime;

    @Column(length = 50)
    private String aircraftModel; // e.g. "Boeing 777-300ER"

    private UUID aircraftId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;

    @Column(length = 20)
    private String gate;

    @Column(length = 20)
    private String terminal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal economyPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal premiumEconomyPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal businessPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal firstClassPrice;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int availableSeats;
}
