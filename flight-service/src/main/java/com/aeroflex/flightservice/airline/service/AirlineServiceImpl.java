package com.aeroflex.flightservice.airline.service;

import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.common.security.SecurityUtils;
import com.aeroflex.flightservice.airline.dto.AirlineCommissionUpdateDto;
import com.aeroflex.flightservice.airline.dto.AirlineDto;
import com.aeroflex.flightservice.airline.dto.CreateAirlineRequest;
import com.aeroflex.flightservice.airline.dto.UpdateAirlineRequest;
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
@Transactional
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AirlineDto> getAllAirlines(
            String search,
            Pageable pageable
    ) {
        Page<Airline> page;

        if (search == null || search.isBlank()) {
            page = airlineRepository.findAll(pageable);
        } else {
            page = airlineRepository.search(
                    search.trim(),
                    pageable
            );
        }

        return page.map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AirlineDto getAirlineById(UUID airlineId) {
        return airlineRepository
                .findById(airlineId)
                .map(this::toDto)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Airline not found"
                        )
                );
    }

    @Override
    public AirlineDto createAirline(
            CreateAirlineRequest request
    ) {
        UUID ownerId = SecurityUtils.getCurrentUserId();

        Airline airline = Airline.builder()
                .airlineName(request.airlineName())
                .iataCode(request.iataCode())
                .headquarters(request.headquarters())
                .country(request.country())
                .contactNumber(request.contactNumber())
                .email(request.email())
                .foundedYear(request.foundedYear())
                .websiteUrl(request.websiteUrl())
                .airlineLogo(request.airlineLogo())
                .commissionPercentage(
                        request.commissionPercentage()
                )
                .ownerId(ownerId)
                .active(true)
                .build();

        Airline saved = airlineRepository.save(airline);

        log.info(
                "Airline created successfully. AirlineId: {}",
                saved.getId()
        );

        return toDto(saved);
    }

    @Override
    public AirlineDto updateAirline(
            UUID airlineId,
            UpdateAirlineRequest request
    ) {
        UUID ownerId = SecurityUtils.getCurrentUserId();

        Airline airline = airlineRepository
                .findByIdAndOwnerId(airlineId, ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Airline not found or ownership mismatch"
                        )
                );

        airline.setAirlineName(request.airlineName());
        airline.setIataCode(request.iataCode());
        airline.setHeadquarters(request.headquarters());
        airline.setCountry(request.country());
        airline.setContactNumber(request.contactNumber());
        airline.setEmail(request.email());
        airline.setFoundedYear(request.foundedYear());
        airline.setWebsiteUrl(request.websiteUrl());
        airline.setAirlineLogo(request.airlineLogo());
        airline.setCommissionPercentage(
                request.commissionPercentage()
        );

        Airline updated = airlineRepository.save(airline);

        log.info(
                "Airline updated successfully. AirlineId: {}",
                updated.getId()
        );

        return toDto(updated);
    }

    @Override
    public void deleteAirline(UUID airlineId) {
        UUID ownerId = SecurityUtils.getCurrentUserId();

        Airline airline = airlineRepository
                .findByIdAndOwnerId(airlineId, ownerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Airline not found or ownership mismatch"
                        )
                );

        airline.setActive(false);

        log.info(
                "Airline soft-deleted successfully. AirlineId: {}",
                airlineId
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AirlineDto> getAllAirlinesByFlightOwner(
            String search,
            Pageable pageable
    ) {
        UUID ownerId = SecurityUtils.getCurrentUserId();
        Page<Airline> page;

        if (search == null || search.isBlank()) {
            page = airlineRepository.findAllByOwnerId(
                    ownerId,
                    pageable
            );
        } else {
            page = airlineRepository.searchByOwner(
                    ownerId,
                    search.trim(),
                    pageable
            );
        }

        return page.map(this::toDto);
    }

    @Override
    public void updateCommissionPercentage(
            UUID airlineId,
            AirlineCommissionUpdateDto dto
    ) {
        Airline airline = airlineRepository
                .findById(airlineId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Airline not found"
                        )
                );

        airline.setCommissionPercentage(
                dto.commissionPercentage()
        );

        airlineRepository.save(airline);

        log.info(
                "Commission updated successfully. AirlineId: {}",
                airlineId
        );
    }

    private AirlineDto toDto(Airline airline) {
        return new AirlineDto(
                airline.getId(),
                airline.getAirlineName(),
                airline.getIataCode(),
                airline.getHeadquarters(),
                airline.getCountry(),
                airline.getContactNumber(),
                airline.getEmail(),
                airline.getFoundedYear(),
                airline.getWebsiteUrl(),
                airline.getAirlineLogo(),
                airline.getCommissionPercentage(),
                airline.getOwnerId()
        );
    }
}
