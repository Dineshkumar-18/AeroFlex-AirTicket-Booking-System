package com.aeroflex.flightservice.airport.service;

import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.flightservice.airport.dto.AirportRequest;
import com.aeroflex.flightservice.airport.dto.AirportResponse;
import com.aeroflex.flightservice.airport.entity.Airport;
import com.aeroflex.flightservice.airport.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    @Override
    @Transactional
    public AirportResponse createAirport(AirportRequest request) {
        if (airportRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new ConflictException("Airport with code " + request.getCode() + " already exists");
        }

        Airport airport = Airport.builder()
                .code(request.getCode().toUpperCase())
                .icaoCode(request.getIcaoCode() != null ? request.getIcaoCode().toUpperCase() : null)
                .name(request.getName())
                .city(request.getCity())
                .country(request.getCountry())
                .timezone(request.getTimezone())
                .active(request.isActive())
                .build();

        Airport saved = airportRepository.save(airport);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AirportResponse> getAirports(
            String search,
            Pageable pageable) {

        if (search == null || search.isBlank()) {
            return airportRepository.findAll(pageable)
                    .map(this::mapToResponse);
        } else {
            return airportRepository.searchAirports(search.trim(), pageable)
                    .map(this::mapToResponse);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AirportResponse getAirportById(UUID id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));
        return mapToResponse(airport);
    }

    @Override
    @Transactional(readOnly = true)
    public AirportResponse getAirportByCode(String code) {
        Airport airport = airportRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with code: " + code));
        return mapToResponse(airport);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AirportResponse> getAllAirports(Pageable pageable) {
        return airportRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public AirportResponse updateAirport(UUID id, AirportRequest request) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with id: " + id));

        if (!airport.getCode().equalsIgnoreCase(request.getCode()) &&
                airportRepository.existsByCode(request.getCode().toUpperCase())) {
            throw new ConflictException("Airport with code " + request.getCode() + " already exists");
        }

        airport.setCode(request.getCode().toUpperCase());
        airport.setIcaoCode(request.getIcaoCode() != null ? request.getIcaoCode().toUpperCase() : null);
        airport.setName(request.getName());
        airport.setCity(request.getCity());
        airport.setCountry(request.getCountry());
        airport.setTimezone(request.getTimezone());
        airport.setActive(request.isActive());

        Airport updated = airportRepository.save(airport);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteAirport(UUID airportId) {
        Airport airport = airportRepository.findById(airportId)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));

        airport.setActive(false);
        airportRepository.save(airport);
    }

    private AirportResponse mapToResponse(Airport airport) {
        return AirportResponse.builder()
                .id(airport.getId())
                .code(airport.getCode())
                .icaoCode(airport.getIcaoCode())
                .name(airport.getName())
                .city(airport.getCity())
                .country(airport.getCountry())
                .timezone(airport.getTimezone())
                .active(airport.isActive())
                .createdAt(airport.getCreatedAt())
                .updatedAt(airport.getUpdatedAt())
                .build();
    }
}
