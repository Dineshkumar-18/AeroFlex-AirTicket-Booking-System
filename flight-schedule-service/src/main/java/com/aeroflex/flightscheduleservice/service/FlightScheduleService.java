package com.aeroflex.flightscheduleservice.service;

import com.aeroflex.flightscheduleservice.dto.FlightScheduleRequest;
import com.aeroflex.flightscheduleservice.dto.FlightScheduleResponse;
import com.aeroflex.flightscheduleservice.dto.ScheduleStatusUpdateRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface FlightScheduleService {
    FlightScheduleResponse createSchedule(FlightScheduleRequest request);
    FlightScheduleResponse getScheduleById(UUID id);
    List<FlightScheduleResponse> searchSchedules(String origin, String destination, LocalDate departureDate);
    List<FlightScheduleResponse> getSchedulesByFlightNumber(String flightNumber);
    FlightScheduleResponse updateSchedule(UUID id, FlightScheduleRequest request);
    FlightScheduleResponse updateScheduleStatus(UUID id, ScheduleStatusUpdateRequest request);
    void deleteSchedule(UUID id);
}
