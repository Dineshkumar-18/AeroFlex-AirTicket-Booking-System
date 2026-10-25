package com.aeroflex.flightservice.airline.service;

import com.aeroflex.flightservice.airline.dto.AirlineCommissionUpdateDto;
import com.aeroflex.flightservice.airline.dto.AirlineDto;
import com.aeroflex.flightservice.airline.dto.CreateAirlineRequest;
import com.aeroflex.flightservice.airline.dto.UpdateAirlineRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AirlineService {

    Page<AirlineDto> getAllAirlines(
            String search,
            Pageable pageable
    );

    AirlineDto getAirlineById(UUID airlineId);

    AirlineDto createAirline(CreateAirlineRequest request);

    AirlineDto updateAirline(
            UUID airlineId,
            UpdateAirlineRequest request
    );

    void deleteAirline(UUID airlineId);

    Page<AirlineDto> getAllAirlinesByFlightOwner(
            String search,
            Pageable pageable
    );

    void updateCommissionPercentage(
            UUID airlineId,
            AirlineCommissionUpdateDto dto
    );
}