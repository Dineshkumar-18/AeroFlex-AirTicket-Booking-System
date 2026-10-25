package com.aeroflex.flightservice.airport.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.flightservice.airport.dto.AirportRequest;
import com.aeroflex.flightservice.airport.dto.AirportResponse;
import com.aeroflex.flightservice.airport.service.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AirportResponse>> createAirport(@Valid @RequestBody AirportRequest request) {
        AirportResponse response = airportService.createAirport(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Airport created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> getAirportById(@PathVariable UUID id) {
        AirportResponse response = airportService.getAirportById(id);
        return ResponseEntity.ok(ApiResponse.success("Airport retrieved successfully", response));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<AirportResponse>> getAirportByCode(@PathVariable String code) {
        AirportResponse response = airportService.getAirportByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Airport retrieved successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AirportResponse>>> getAirports(@PageableDefault(size = 10, sort = "code") Pageable pageable) {
        Page<AirportResponse> response = airportService.getAllAirports(pageable);
        return ResponseEntity.ok(ApiResponse.success("Airports retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AirportResponse>> updateAirport(
            @PathVariable UUID id,
            @Valid @RequestBody AirportRequest request) {
        AirportResponse response = airportService.updateAirport(id, request);
        return ResponseEntity.ok(ApiResponse.success("Airport updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAirport(@PathVariable UUID id) {
        airportService.deleteAirport(id);
        return ResponseEntity.ok(ApiResponse.success("Airport deleted successfully"));
    }
}
