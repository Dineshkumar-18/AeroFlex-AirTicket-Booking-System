package com.aeroflex.flightservice.flight.service;

import com.aeroflex.flightservice.flight.dto.FlightRequest;
import com.aeroflex.flightservice.flight.dto.FlightResponse;
import com.aeroflex.flightservice.flight.dto.FlightStatusChangeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FlightService {
    FlightResponse createFlight(FlightRequest request);
    FlightResponse getFlightById(UUID id);
    FlightResponse getFlightByNumber(String flightNumber);
    Page<FlightResponse> getAllFlights(Pageable pageable);
    Page<FlightResponse> searchFlights(String origin, String destination, Pageable pageable);
    FlightResponse updateFlight(UUID id, FlightRequest request);
    FlightResponse updateFlightStatus(UUID id, FlightStatusChangeRequest request);
    void deleteFlight(UUID id);
}
