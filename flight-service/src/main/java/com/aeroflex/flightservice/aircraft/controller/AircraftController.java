package com.aeroflex.flightservice.aircraft.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.flightservice.aircraft.dto.AircraftRequest;
import com.aeroflex.flightservice.aircraft.dto.AircraftResponse;
import com.aeroflex.flightservice.aircraft.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/aircrafts")
@RequiredArgsConstructor
public class AircraftController {

    private final AircraftService aircraftService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AircraftResponse>>
    createAircraft(
            @Valid @RequestBody AircraftRequest request) {

        AircraftResponse response =
                aircraftService.createAircraft(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Aircraft created successfully",
                        response
                ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FLIGHT_OWNER','ADMIN')")
    public ResponseEntity<ApiResponse<AircraftResponse>>
    getAircraftById(
            @PathVariable UUID id) {

        AircraftResponse response =
                aircraftService.getAircraftById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft retrieved successfully",
                        response
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<AircraftResponse>>> getAircrafts(
            @RequestParam(required = false) String search,
            @PageableDefault(
                    size = 10,
                    sort = "registrationNumber",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {

        Page<AircraftResponse> response =
                aircraftService.getAircrafts(
                        search,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircrafts retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/airline/{airlineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FLIGHT_OWNER')")
    public ResponseEntity<ApiResponse<Page<AircraftResponse>>>
    getAircraftByAirline(
            @PathVariable UUID airlineId,
            @PageableDefault(
                    size = 10,
                    sort = "registrationNumber",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {

        Page<AircraftResponse> response =
                aircraftService.getAircraftsByAirline(
                        airlineId,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft retrieved successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AircraftResponse>>
    updateAircraft(
            @PathVariable UUID id,
            @Valid @RequestBody AircraftRequest request) {

        AircraftResponse response =
                aircraftService.updateAircraft(
                        id,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>>
    deleteAircraft(
            @PathVariable UUID id) {

        aircraftService.deleteAircraft(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft deleted successfully"
                )
        );
    }
}
