package com.aeroflex.flightservice.flight.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.flightservice.flight.dto.FlightRequest;
import com.aeroflex.flightservice.flight.dto.FlightResponse;
import com.aeroflex.flightservice.flight.dto.FlightStatusChangeRequest;
import com.aeroflex.flightservice.flight.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FlightResponse>> createFlight(@Valid @RequestBody FlightRequest request) {
        FlightResponse response = flightService.createFlight(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Flight created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightResponse>> getFlightById(@PathVariable UUID id) {
        FlightResponse response = flightService.getFlightById(id);
        return ResponseEntity.ok(ApiResponse.success("Flight retrieved successfully", response));
    }

    @GetMapping("/number/{flightNumber}")
    public ResponseEntity<ApiResponse<FlightResponse>> getFlightByNumber(@PathVariable String flightNumber) {
        FlightResponse response = flightService.getFlightByNumber(flightNumber);
        return ResponseEntity.ok(ApiResponse.success("Flight retrieved successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FlightResponse>>> getAllFlights(
            @PageableDefault(size = 10, sort = "flightNumber") Pageable pageable) {
        Page<FlightResponse> response = flightService.getAllFlights(pageable);
        return ResponseEntity.ok(ApiResponse.success("Flights retrieved successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<FlightResponse>>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @PageableDefault(size = 10, sort = "flightNumber") Pageable pageable) {
        Page<FlightResponse> response = flightService.searchFlights(origin, destination, pageable);
        return ResponseEntity.ok(ApiResponse.success("Flights retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FlightResponse>> updateFlight(
            @PathVariable UUID id,
            @Valid @RequestBody FlightRequest request) {
        FlightResponse response = flightService.updateFlight(id, request);
        return ResponseEntity.ok(ApiResponse.success("Flight updated successfully", response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<FlightResponse>> updateFlightStatus(
            @PathVariable UUID id,
            @Valid @RequestBody FlightStatusChangeRequest request) {
        FlightResponse response = flightService.updateFlightStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Flight status updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteFlight(@PathVariable UUID id) {
        flightService.deleteFlight(id);
        return ResponseEntity.ok(ApiResponse.success("Flight deleted successfully"));
    }
}
