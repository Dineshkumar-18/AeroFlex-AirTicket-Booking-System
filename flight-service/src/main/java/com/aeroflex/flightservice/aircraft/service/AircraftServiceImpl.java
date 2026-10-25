package com.aeroflex.flightservice.aircraft.service;

import com.aeroflex.common.enums.RoleType;
import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.common.exception.InvalidStatusTransitionException;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.common.security.SecurityUtils;
import com.aeroflex.flightservice.aircraft.dto.AircraftRequest;
import com.aeroflex.flightservice.aircraft.dto.AircraftResponse;
import com.aeroflex.flightservice.aircraft.entity.Aircraft;
import com.aeroflex.flightservice.aircraft.entity.AircraftType;
import com.aeroflex.flightservice.aircraft.enums.AircraftStatus;
import com.aeroflex.flightservice.aircraft.repository.AircraftRepository;
import com.aeroflex.flightservice.aircraft.repository.AircraftTypeRepository;
import com.aeroflex.flightservice.airline.entity.Airline;
import com.aeroflex.flightservice.airline.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AircraftServiceImpl
        implements AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AircraftTypeRepository aircraftTypeRepository;
    private final AirlineRepository airlineRepository;

    @Override
    @Transactional
    public AircraftResponse createAircraft(
            AircraftRequest request) {

        String registration =
                request.registrationNumber()
                        .trim()
                        .toUpperCase();

        log.info(
                "Creating aircraft with registration number: {}",
                registration
        );

        if (aircraftRepository
                .existsByRegistrationNumberIgnoreCase(registration)) {

            log.warn(
                    "Aircraft creation failed: registration number '{}' already exists",
                    registration
            );


            throw new ConflictException(
                    "Aircraft already exists!"
            );
        }

        Airline airline = airlineRepository.findByIdIsActiveTrue(request.airlineId())
                .orElseThrow(() -> {

                    log.warn(
                            "Aircraft creation failed: airline not found, airlineId={}",
                            request.airlineId()
                    );

                 return new ResourceNotFoundException("Airline not found");
                });

        AircraftType aircraftType =
                aircraftTypeRepository
                        .findByIdAndIsActiveTrue(request.aircraftTypeId())
                        .orElseThrow(() -> {
                            log.warn(
                                    "Aircraft creation failed: aircraft type not found, aircraftTypeId={}",
                                    request.aircraftTypeId()
                            );
                                   return new ResourceNotFoundException(
                                            "Aircraft type not found"
                                    );
                                }
                        );


        Aircraft aircraft = Aircraft.builder()
                .registrationNumber(registration)
                .airline(airline)
                .aircraftType(aircraftType)
                .name(
                        request.name() == null
                                ? null
                                : request.name().trim()
                )
                .status(AircraftStatus.ACTIVE)
                .build();

        Aircraft saved =
                aircraftRepository.save(aircraft);

        log.info(
                "Aircraft created successfully: aircraftId={}, registrationNumber={}",
                saved.getId(),
                saved.getRegistrationNumber()
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AircraftResponse getAircraftById(UUID id) {

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Aircraft not found: aircraftId={}", id);
                    return new ResourceNotFoundException("Aircraft not found");
                });

        boolean isAdmin =
                SecurityUtils.isRoleMatch(RoleType.ADMIN.name());

        boolean isFlightOwner =
                SecurityUtils.isRoleMatch(RoleType.FLIGHT_OWNER.name());

        if (isAdmin) {
            return mapToResponse(aircraft);
        }

        if (!isFlightOwner) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Access denied"
            );
        }

        UUID flightOwnerId = SecurityUtils.getCurrentUserId();

        boolean ownsAirline = airlineRepository.existsByIdAndOwnerId(
                aircraft.getAirline().getId(),
                flightOwnerId
        );

        if (!ownsAirline || !aircraft.isActive()) {
            log.warn(
                    "Aircraft access denied: aircraftId={}, flightOwnerId={}",
                    id,
                    flightOwnerId
            );

            throw new ResourceNotFoundException("Aircraft not found");
        }

        return mapToResponse(aircraft);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AircraftResponse> getAircrafts(
            String search,
            Pageable pageable) {

        Page<Aircraft> aircraftPage;

        if (search == null || search.isBlank()) {
            aircraftPage =
                    aircraftRepository.findAll(pageable);
        } else {
            aircraftPage =
                    aircraftRepository.search(
                            search.trim(),
                            pageable
                    );
        }

        log.debug(
                "Aircraft list retrieved successfully: totalElements={}",
                aircraftPage.getTotalElements()
        );

        return aircraftPage.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AircraftResponse>
    getAircraftsByAirline(
            UUID airlineId,
            Pageable pageable) {

        boolean isAdmin = SecurityUtils.isRoleMatch(RoleType.ADMIN.name());

        if(!isAdmin) {
            UUID flightOwnerId = SecurityUtils.getCurrentUserId();
            boolean flightOwnerExist = airlineRepository.existsByIdAndOwnerId(airlineId, flightOwnerId);

            if(!flightOwnerExist) {
                log.warn(
                        "Airline access denied: airlineId={}, flightOwnerId={}",
                        airlineId,
                        flightOwnerId
                );

                throw new ResourceNotFoundException("Airline not found");
            }

            return aircraftRepository
                    .findByAirlineIdAndIsActiveTrue(airlineId,pageable)
                    .map(this::mapToResponse);

        }
        return aircraftRepository
                .findByAirlineId(airlineId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public AircraftResponse updateAircraft(
            UUID id,
            AircraftRequest request) {

        Aircraft aircraft =
                aircraftRepository.findByIdAndIsActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft not found"
                                )
                        );

        String registration =
                request.registrationNumber()
                        .trim()
                        .toUpperCase();

        if (aircraftRepository
                .existsByRegistrationNumberIgnoreCaseAndIdNot(
                        registration,
                        id
                )) {

            log.warn(
                    "Aircraft update failed: registration number '{}' already exists, aircraftId={}",
                    registration,
                    id
            );

            throw new ConflictException(
                    "Aircraft with registration number '"
                            + registration
                            + "' already exists"
            );
        }

        Airline airline = airlineRepository.findById(request.airlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        AircraftType aircraftType =
                aircraftTypeRepository
                        .findById(request.aircraftTypeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft type not found"
                                )
                        );

        aircraft.setRegistrationNumber(registration);
        aircraft.setAirline(airline);
        aircraft.setAircraftType(aircraftType);
        aircraft.setName(
                request.name() == null
                        ? null
                        : request.name().trim()
        );

        log.info(
                "Aircraft updated successfully: aircraftId={}, registrationNumber={}",
                aircraft.getId(),
                aircraft.getRegistrationNumber()
        );

        return mapToResponse(aircraft);
    }

    @Override
    @Transactional
    public void deleteAircraft(UUID id) {

        Aircraft aircraft =
                aircraftRepository.findByIdAndIsActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft not found"
                                )
                        );

        aircraft.setActive(false);
        log.info("Aircraft soft-deleted successfully: aircraftId={}", id);
    }

    @Override
    @Transactional
    public void undoDeleteAircraft(UUID id) {
        Aircraft aircraft =
                aircraftRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Aircraft not found"
                                )
                        );
        aircraft.setActive(true);

        log.info("Aircraft soft-delete reversed successfully: aircraftId={}", id);
    }

    private AircraftResponse mapToResponse(Aircraft aircraft) {
        AircraftType type = aircraft.getAircraftType();

        return new AircraftResponse(
                aircraft.getId(),
                aircraft.getRegistrationNumber(),
                aircraft.getAirline().getId(),
                type.getId(),
                type.getModel(),
                type.getManufacturer(),
                type.getTotalCapacity(),
                aircraft.getName(),
                aircraft.getStatus(),
                aircraft.isActive()
        );
    }

    @Transactional
    public void changeStatus(
            UUID aircraftId,
            AircraftStatus newStatus) {

        Aircraft aircraft = aircraftRepository.findById(aircraftId)
                .orElseThrow(() -> {
                    log.warn(
                            "Aircraft status change failed: aircraft not found, aircraftId={}",
                            aircraftId
                    );
                        return new ResourceNotFoundException("Aircraft not found");
                });

        AircraftStatus currentStatus = aircraft.getStatus();

        if (!currentStatus.canTransitionTo(newStatus)) {

            log.warn(
                    "Invalid aircraft status transition: aircraftId={}, currentStatus={}, requestedStatus={}",
                    aircraftId,
                    currentStatus,
                    newStatus
            );

            throw new InvalidStatusTransitionException(
                    "Invalid status transition: "
                            + currentStatus + " -> " + newStatus
            );
        }

        aircraft.setStatus(newStatus);

        log.info(
                "Aircraft status changed successfully: aircraftId={}, previousStatus={}, newStatus={}",
                aircraftId,
                currentStatus,
                newStatus
        );
    }

}