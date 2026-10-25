package com.aeroflex.flightservice.aircraft.service;

import com.aeroflex.common.response.PagedResponse;
import com.aeroflex.flightservice.aircraft.dto.AircraftTypeRequest;
import com.aeroflex.flightservice.aircraft.dto.AircraftTypeResponse;
import com.aeroflex.flightservice.aircraft.enums.AircraftStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AircraftTypeService {

    AircraftTypeResponse createAircraftType(
            AircraftTypeRequest request
    );

    AircraftTypeResponse getAircraftTypeById(
            UUID id
    );

    AircraftTypeResponse getAircraftTypeByIdIncludingInactive(UUID id);

    Page<AircraftTypeResponse> getAircraftTypes(
            String search,
            Pageable pageable
    );

    AircraftTypeResponse updateAircraftType(
            UUID id,
            AircraftTypeRequest request
    );

    void deleteAircraftType(UUID id);
}
