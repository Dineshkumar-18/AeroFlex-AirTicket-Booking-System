package com.aeroflex.flightservice.flight.service;

import com.aeroflex.common.enums.FlightStatus;
import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.flightservice.aircraft.entity.Aircraft;
import com.aeroflex.flightservice.aircraft.repository.AircraftRepository;
import com.aeroflex.flightservice.airline.dto.AirlineDto;
import com.aeroflex.flightservice.airline.entity.Airline;
import com.aeroflex.flightservice.airline.repository.AirlineRepository;
import com.aeroflex.flightservice.airport.dto.AirportResponse;
import com.aeroflex.flightservice.airport.entity.Airport;
import com.aeroflex.flightservice.airport.repository.AirportRepository;
import com.aeroflex.flightservice.flight.dto.FlightRequest;
import com.aeroflex.flightservice.flight.dto.FlightResponse;
import com.aeroflex.flightservice.flight.dto.FlightStatusChangeRequest;
import com.aeroflex.flightservice.flight.entity.Flight;
import com.aeroflex.flightservice.flight.repository.FlightRepository;
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
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AircraftRepository aircraftRepository;
    private final AirportRepository airportRepository;

    @Override
    @Transactional
    public FlightResponse createFlight(FlightRequest request) {
        if (flightRepository.existsByFlightNumber(request.getFlightNumber().toUpperCase())) {
            throw new ConflictException("Flight with number " + request.getFlightNumber() + " already exists");
        }

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + request.getAirlineId()));

        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + request.getAircraftId()));

        Airport departureAirport = airportRepository.findById(request.getDepartureAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Departure airport not found with id: " + request.getDepartureAirportId()));

        Airport arrivalAirport = airportRepository.findById(request.getArrivalAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found with id: " + request.getArrivalAirportId()));

        if (departureAirport.getId().equals(arrivalAirport.getId())) {
            throw new IllegalArgumentException("Departure and arrival airports cannot be the same");
        }

        FlightStatus status = request.getStatus() != null ? request.getStatus() : FlightStatus.SCHEDULED;

        Flight flight = Flight.builder()
                .flightNumber(request.getFlightNumber().toUpperCase())
                .airline(airline)
                .aircraft(aircraft)
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .durationMinutes(request.getDurationMinutes())
                .distanceKm(request.getDistanceKm())
                .status(status)
                .active(request.isActive())
                .build();

        Flight saved = flightRepository.save(flight);
        log.info("Flight created successfully with ID: {} and status: {}", saved.getId(), saved.getStatus());
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse getFlightById(UUID id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        return mapToResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightResponse getFlightByNumber(String flightNumber) {
        Flight flight = flightRepository.findByFlightNumber(flightNumber.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with number: " + flightNumber));
        return mapToResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> getAllFlights(Pageable pageable) {
        return flightRepository.findAllWithDetails(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightResponse> searchFlights(String origin, String destination, Pageable pageable) {
        return flightRepository.findByOriginAndDestination(origin.toUpperCase(), destination.toUpperCase(), pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional
    public FlightResponse updateFlight(UUID id, FlightRequest request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        if (!flight.getFlightNumber().equalsIgnoreCase(request.getFlightNumber()) &&
                flightRepository.existsByFlightNumber(request.getFlightNumber().toUpperCase())) {
            throw new ConflictException("Flight with number " + request.getFlightNumber() + " already exists");
        }

        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + request.getAirlineId()));

        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId())
                .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with id: " + request.getAircraftId()));

        Airport departureAirport = airportRepository.findById(request.getDepartureAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Departure airport not found with id: " + request.getDepartureAirportId()));

        Airport arrivalAirport = airportRepository.findById(request.getArrivalAirportId())
                .orElseThrow(() -> new ResourceNotFoundException("Arrival airport not found with id: " + request.getArrivalAirportId()));

        flight.setFlightNumber(request.getFlightNumber().toUpperCase());
        flight.setAirline(airline);
        flight.setAircraft(aircraft);
        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);
        flight.setDurationMinutes(request.getDurationMinutes());
        flight.setDistanceKm(request.getDistanceKm());
        if (request.getStatus() != null) {
            flight.setStatus(request.getStatus());
        }
        flight.setActive(request.isActive());

        Flight updated = flightRepository.save(flight);
        log.info("Flight updated successfully with ID: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public FlightResponse updateFlightStatus(UUID id, FlightStatusChangeRequest request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        flight.setStatus(request.getStatus());
        Flight updated = flightRepository.save(flight);
        log.info("Flight status updated to {} for flight ID: {}", request.getStatus(), id);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteFlight(UUID id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));
        flight.setActive(false);
        log.info("Flight soft-deleted with ID: {}", id);
    }

    private FlightResponse mapToResponse(Flight flight) {
        return FlightResponse.builder()
                .id(flight.getId())
                .flightNumber(flight.getFlightNumber())
                .airlineId(flight.getAirline().getId())
                .aircraftId(flight.getAircraft() != null ? flight.getAircraft().getId() : null)
                .airline(new AirlineDto(
                        flight.getAirline().getId(),
                        flight.getAirline().getAirlineName(),
                        flight.getAirline().getIataCode(),
                        flight.getAirline().getHeadquarters(),
                        flight.getAirline().getCountry(),
                        flight.getAirline().getContactNumber(),
                        flight.getAirline().getEmail(),
                        flight.getAirline().getFoundedYear(),
                        flight.getAirline().getWebsiteUrl(),
                        flight.getAirline().getAirlineLogo(),
                        flight.getAirline().getCommissionPercentage(),
                        flight.getAirline().getOwnerId()
                ))
                .departureAirport(AirportResponse.builder()
                        .id(flight.getDepartureAirport().getId())
                        .code(flight.getDepartureAirport().getCode())
                        .icaoCode(flight.getDepartureAirport().getIcaoCode())
                        .name(flight.getDepartureAirport().getName())
                        .city(flight.getDepartureAirport().getCity())
                        .country(flight.getDepartureAirport().getCountry())
                        .timezone(flight.getDepartureAirport().getTimezone())
                        .active(flight.getDepartureAirport().isActive())
                        .createdAt(flight.getDepartureAirport().getCreatedAt())
                        .updatedAt(flight.getDepartureAirport().getUpdatedAt())
                        .build())
                .arrivalAirport(AirportResponse.builder()
                        .id(flight.getArrivalAirport().getId())
                        .code(flight.getArrivalAirport().getCode())
                        .icaoCode(flight.getArrivalAirport().getIcaoCode())
                        .name(flight.getArrivalAirport().getName())
                        .city(flight.getArrivalAirport().getCity())
                        .country(flight.getArrivalAirport().getCountry())
                        .timezone(flight.getArrivalAirport().getTimezone())
                        .active(flight.getArrivalAirport().isActive())
                        .createdAt(flight.getArrivalAirport().getCreatedAt())
                        .updatedAt(flight.getArrivalAirport().getUpdatedAt())
                        .build())
                .durationMinutes(flight.getDurationMinutes())
                .distanceKm(flight.getDistanceKm())
                .status(flight.getStatus())
                .active(flight.isActive())
                .createdAt(flight.getCreatedAt())
                .updatedAt(flight.getUpdatedAt())
                .build();
    }
}
