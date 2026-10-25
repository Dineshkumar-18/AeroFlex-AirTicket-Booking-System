package com.aeroflex.flightservice.aircraft.service;

import com.aeroflex.flightservice.aircraft.dto.AircraftRequest;
import com.aeroflex.flightservice.aircraft.dto.AircraftResponse;
import com.aeroflex.flightservice.aircraft.enums.AircraftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AircraftService {

    AircraftResponse createAircraft(
            AircraftRequest request
    );

    AircraftResponse getAircraftById(
            UUID id
    );

    Page<AircraftResponse> getAircrafts(
            String search,
            Pageable pageable
    );

    Page<AircraftResponse> getAircraftsByAirline(
            UUID airlineId,
            Pageable pageable
    );

    AircraftResponse updateAircraft(
            UUID id,
            AircraftRequest request
    );

    void deleteAircraft(UUID id);

    void undoDeleteAircraft(UUID id);

    void changeStatus(UUID id, AircraftStatus newStatus);
}
