package com.aeroflex.flightservice.flight.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.FlightStatus;
import com.aeroflex.flightservice.aircraft.entity.Aircraft;
import com.aeroflex.flightservice.airline.entity.Airline;
import com.aeroflex.flightservice.airport.entity.Airport;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String flightNumber; // e.g. "AI-101", "6E-205"

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "airline_id", nullable = false, foreignKey =
    @ForeignKey(name = "fk_flight_airline"))
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "aircraft_id",nullable = false,
            foreignKey = @ForeignKey(name = "fk_flight_aircraft"))
    private Aircraft aircraft;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departure_airport_id", nullable = false)
    private Airport departureAirport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrival_airport_id", nullable = false)
    private Airport arrivalAirport;

    @Column(nullable = false)
    private int durationMinutes; // e.g. 120 (2 hrs)

    @Column(nullable = false)
    private int distanceKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FlightStatus status = FlightStatus.SCHEDULED;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
