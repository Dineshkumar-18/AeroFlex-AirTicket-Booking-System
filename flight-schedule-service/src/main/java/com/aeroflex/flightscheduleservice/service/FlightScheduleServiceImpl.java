package com.aeroflex.flightscheduleservice.service;

import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.flightscheduleservice.dto.FlightScheduleRequest;
import com.aeroflex.flightscheduleservice.dto.FlightScheduleResponse;
import com.aeroflex.flightscheduleservice.dto.ScheduleStatusUpdateRequest;
import com.aeroflex.flightscheduleservice.entity.FlightSchedule;
import com.aeroflex.flightscheduleservice.repository.FlightScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public FlightScheduleResponse createSchedule(FlightScheduleRequest request) {
        FlightSchedule schedule = FlightSchedule.builder()
                .flightNumber(request.getFlightNumber().toUpperCase())
                .flightId(request.getFlightId())
                .airlineCode(request.getAirlineCode().toUpperCase())
                .airlineName(request.getAirlineName())
                .departureAirportCode(request.getDepartureAirportCode().toUpperCase())
                .arrivalAirportCode(request.getArrivalAirportCode().toUpperCase())
                .departureCity(request.getDepartureCity())
                .arrivalCity(request.getArrivalCity())
                .scheduledDepartureTime(request.getScheduledDepartureTime())
                .scheduledArrivalTime(request.getScheduledArrivalTime())
                .aircraftModel(request.getAircraftModel())
                .aircraftId(request.getAircraftId())
                .status(request.getStatus())
                .gate(request.getGate())
                .terminal(request.getTerminal())
                .economyPrice(request.getEconomyPrice())
                .premiumEconomyPrice(request.getPremiumEconomyPrice())
                .businessPrice(request.getBusinessPrice())
                .firstClassPrice(request.getFirstClassPrice())
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getAvailableSeats() > 0 ? request.getAvailableSeats() : request.getTotalSeats())
                .build();

        FlightSchedule saved = scheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FlightScheduleResponse getScheduleById(UUID id) {
        FlightSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight schedule not found with id: " + id));
        return mapToResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightScheduleResponse> searchSchedules(String origin, String destination, LocalDate departureDate) {
        List<FlightSchedule> schedules;
        if (departureDate != null) {
            LocalDateTime start = departureDate.atStartOfDay();
            LocalDateTime end = departureDate.atTime(LocalTime.MAX);
            schedules = scheduleRepository.searchSchedules(origin.toUpperCase(), destination.toUpperCase(), start, end);
        } else {
            schedules = scheduleRepository.findByOriginAndDestination(origin.toUpperCase(), destination.toUpperCase());
        }

        return schedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightScheduleResponse> getSchedulesByFlightNumber(String flightNumber) {
        return scheduleRepository.findByFlightNumber(flightNumber.toUpperCase()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FlightScheduleResponse updateSchedule(UUID id, FlightScheduleRequest request) {
        FlightSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight schedule not found with id: " + id));

        schedule.setFlightNumber(request.getFlightNumber().toUpperCase());
        schedule.setFlightId(request.getFlightId());
        schedule.setAirlineCode(request.getAirlineCode().toUpperCase());
        schedule.setAirlineName(request.getAirlineName());
        schedule.setDepartureAirportCode(request.getDepartureAirportCode().toUpperCase());
        schedule.setArrivalAirportCode(request.getArrivalAirportCode().toUpperCase());
        schedule.setDepartureCity(request.getDepartureCity());
        schedule.setArrivalCity(request.getArrivalCity());
        schedule.setScheduledDepartureTime(request.getScheduledDepartureTime());
        schedule.setScheduledArrivalTime(request.getScheduledArrivalTime());
        schedule.setAircraftModel(request.getAircraftModel());
        schedule.setAircraftId(request.getAircraftId());
        schedule.setStatus(request.getStatus());
        schedule.setGate(request.getGate());
        schedule.setTerminal(request.getTerminal());
        schedule.setEconomyPrice(request.getEconomyPrice());
        schedule.setPremiumEconomyPrice(request.getPremiumEconomyPrice());
        schedule.setBusinessPrice(request.getBusinessPrice());
        schedule.setFirstClassPrice(request.getFirstClassPrice());
        schedule.setTotalSeats(request.getTotalSeats());
        schedule.setAvailableSeats(request.getAvailableSeats());

        FlightSchedule updated = scheduleRepository.save(schedule);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public FlightScheduleResponse updateScheduleStatus(UUID id, ScheduleStatusUpdateRequest request) {
        FlightSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight schedule not found with id: " + id));

        schedule.setStatus(request.getStatus());
        if (request.getActualDepartureTime() != null) {
            schedule.setActualDepartureTime(request.getActualDepartureTime());
        }
        if (request.getActualArrivalTime() != null) {
            schedule.setActualArrivalTime(request.getActualArrivalTime());
        }
        if (request.getGate() != null) {
            schedule.setGate(request.getGate());
        }
        if (request.getTerminal() != null) {
            schedule.setTerminal(request.getTerminal());
        }

        FlightSchedule updated = scheduleRepository.save(schedule);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteSchedule(UUID id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Flight schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }

    private FlightScheduleResponse mapToResponse(FlightSchedule schedule) {
        return FlightScheduleResponse.builder()
                .id(schedule.getId())
                .flightNumber(schedule.getFlightNumber())
                .flightId(schedule.getFlightId())
                .airlineCode(schedule.getAirlineCode())
                .airlineName(schedule.getAirlineName())
                .departureAirportCode(schedule.getDepartureAirportCode())
                .arrivalAirportCode(schedule.getArrivalAirportCode())
                .departureCity(schedule.getDepartureCity())
                .arrivalCity(schedule.getArrivalCity())
                .scheduledDepartureTime(schedule.getScheduledDepartureTime())
                .scheduledArrivalTime(schedule.getScheduledArrivalTime())
                .actualDepartureTime(schedule.getActualDepartureTime())
                .actualArrivalTime(schedule.getActualArrivalTime())
                .aircraftModel(schedule.getAircraftModel())
                .aircraftId(schedule.getAircraftId())
                .status(schedule.getStatus())
                .gate(schedule.getGate())
                .terminal(schedule.getTerminal())
                .economyPrice(schedule.getEconomyPrice())
                .premiumEconomyPrice(schedule.getPremiumEconomyPrice())
                .businessPrice(schedule.getBusinessPrice())
                .firstClassPrice(schedule.getFirstClassPrice())
                .totalSeats(schedule.getTotalSeats())
                .availableSeats(schedule.getAvailableSeats())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
