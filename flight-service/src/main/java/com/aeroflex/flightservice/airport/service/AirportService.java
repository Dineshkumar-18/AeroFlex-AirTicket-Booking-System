package com.aeroflex.flightservice.airport.service;

import com.aeroflex.flightservice.airport.dto.AirportRequest;
import com.aeroflex.flightservice.airport.dto.AirportResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface AirportService {
    AirportResponse createAirport(AirportRequest request);

    AirportResponse getAirportById(UUID id);

    AirportResponse getAirportByCode(String code);

    Page<AirportResponse> getAllAirports(Pageable pageable);

    AirportResponse updateAirport(UUID id, AirportRequest request);

    void deleteAirport(UUID id);

    Page<AirportResponse> getAirports(String search, Pageable pageable);
}
