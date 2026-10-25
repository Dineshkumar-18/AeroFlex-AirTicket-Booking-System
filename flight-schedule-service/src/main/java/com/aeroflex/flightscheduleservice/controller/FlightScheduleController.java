package com.aeroflex.flightscheduleservice.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.flightscheduleservice.dto.FlightScheduleRequest;
import com.aeroflex.flightscheduleservice.dto.FlightScheduleResponse;
import com.aeroflex.flightscheduleservice.dto.ScheduleStatusUpdateRequest;
import com.aeroflex.flightscheduleservice.service.FlightScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class FlightScheduleController {

    private final FlightScheduleService scheduleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> createSchedule(
            @Valid @RequestBody FlightScheduleRequest request) {
        FlightScheduleResponse response = scheduleService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Flight schedule created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> getScheduleById(@PathVariable UUID id) {
        FlightScheduleResponse response = scheduleService.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.success("Flight schedule retrieved successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FlightScheduleResponse>>> searchSchedules(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate) {
        List<FlightScheduleResponse> response = scheduleService.searchSchedules(origin, destination, departureDate);
        return ResponseEntity.ok(ApiResponse.success("Flight schedules retrieved successfully", response));
    }

    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<ApiResponse<List<FlightScheduleResponse>>> getSchedulesByFlightNumber(
            @PathVariable String flightNumber) {
        List<FlightScheduleResponse> response = scheduleService.getSchedulesByFlightNumber(flightNumber);
        return ResponseEntity.ok(ApiResponse.success("Flight schedules retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> updateSchedule(
            @PathVariable UUID id,
            @Valid @RequestBody FlightScheduleRequest request) {
        FlightScheduleResponse response = scheduleService.updateSchedule(id, request);
        return ResponseEntity.ok(ApiResponse.success("Flight schedule updated successfully", response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FlightScheduleResponse>> updateScheduleStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ScheduleStatusUpdateRequest request) {
        FlightScheduleResponse response = scheduleService.updateScheduleStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Flight schedule status updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(@PathVariable UUID id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok(ApiResponse.success("Flight schedule deleted successfully"));
    }
}
