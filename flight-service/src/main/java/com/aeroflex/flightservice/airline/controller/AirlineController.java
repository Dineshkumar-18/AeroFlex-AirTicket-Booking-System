package com.aeroflex.flightservice.airline.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.flightservice.airline.dto.*;
import com.aeroflex.flightservice.airline.service.AirlineService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/airlines")
@RequiredArgsConstructor
public class AirlineController {

    private final AirlineService airlineService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AirlineDto>>> getAllAirlines(
            @RequestParam(required = false) String search,
            @PageableDefault(
                    size = 10,
                    sort = "airlineName",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<AirlineDto> response =
                airlineService.getAllAirlines(
                        search,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Airlines retrieved successfully",
                        response
                )
        );
    }

    @GetMapping("/{airlineId}")
    public ResponseEntity<ApiResponse<AirlineDto>> getAirlineById(
            @PathVariable UUID airlineId
    ) {

        AirlineDto airline =
                airlineService.getAirlineById(airlineId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Airline retrieved successfully",
                        airline
                )
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('FLIGHT_OWNER')")
    public ResponseEntity<ApiResponse<Page<AirlineDto>>>
    getMyAirlines(
            @RequestParam(required = false) String search,
            @PageableDefault(
                    size = 10,
                    sort = "airlineName",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {

        Page<AirlineDto> response =
                airlineService.getAllAirlinesByFlightOwner(
                        search,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Airlines retrieved successfully",
                        response
                )
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('FLIGHT_OWNER')")
    public ResponseEntity<ApiResponse<AirlineDto>> createAirline(
            @Valid @RequestBody CreateAirlineRequest request
    ) {

        AirlineDto airline =
                airlineService.createAirline(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Airline created successfully",
                                airline
                        )
                );
    }

    @PutMapping("/{airlineId}")
    @PreAuthorize("hasRole('FLIGHT_OWNER')")
    public ResponseEntity<ApiResponse<AirlineDto>> updateAirline(
            @PathVariable UUID airlineId,
            @Valid @RequestBody UpdateAirlineRequest request
    ) {

        AirlineDto airline =
                airlineService.updateAirline(
                        airlineId,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Airline updated successfully",
                        airline
                )
        );
    }

    @DeleteMapping("/{airlineId}")
    @PreAuthorize("hasRole('FLIGHT_OWNER')")
    public ResponseEntity<ApiResponse<Void>> deleteAirline(
            @PathVariable UUID airlineId
    ) {

        airlineService.deleteAirline(airlineId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Airline deleted successfully",
                        null
                )
        );
    }

    @PatchMapping("/{airlineId}/commission")
    @PreAuthorize("hasRole('FLIGHT_OWNER')")
    public ResponseEntity<ApiResponse<Void>>
    updateCommissionPercentage(
            @PathVariable UUID airlineId,
            @Valid @RequestBody AirlineCommissionUpdateDto dto
    ) {

        airlineService.updateCommissionPercentage(
                airlineId,
                dto
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Commission percentage updated successfully",
                        null
                )
        );
    }
}
