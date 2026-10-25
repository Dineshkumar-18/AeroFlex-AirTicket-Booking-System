package com.aeroflex.seatservice.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.seatservice.dto.*;
import com.aeroflex.seatservice.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResponse<List<ScheduleSeatResponse>>> getSeatMap(@PathVariable UUID scheduleId) {
        List<ScheduleSeatResponse> response = seatService.getSeatMap(scheduleId);
        return ResponseEntity.ok(ApiResponse.success("Seat map retrieved successfully", response));
    }

    @PostMapping("/lock")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<ApiResponse<SeatLockResponse>> lockSeats(@Valid @RequestBody SeatLockRequest request) {
        SeatLockResponse response = seatService.lockSeats(request);
        return ResponseEntity.ok(ApiResponse.success("Seats locked successfully for 10 minutes", response));
    }

    @PostMapping("/release")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> releaseSeats(@Valid @RequestBody SeatReleaseRequest request) {
        seatService.releaseSeats(request);
        return ResponseEntity.ok(ApiResponse.success("Seats released successfully"));
    }

    @PostMapping("/confirm")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> confirmSeats(@Valid @RequestBody SeatConfirmRequest request) {
        seatService.confirmSeats(request);
        return ResponseEntity.ok(ApiResponse.success("Seats confirmed and allocated successfully"));
    }

    @PostMapping("/schedule/{scheduleId}/init")
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<ScheduleSeatResponse>>> initializeSeatLayout(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody SeatLayoutInitRequest request) {
        List<ScheduleSeatResponse> response = seatService.initializeSeatLayout(
                scheduleId, request.getAircraftModel(), request.getBaseEconomyPrice()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Seat layout initialized successfully", response));
    }
}
