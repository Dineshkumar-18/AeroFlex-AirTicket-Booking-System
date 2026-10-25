package com.aeroflex.flightservice.aircraft.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.common.response.PagedResponse;
import com.aeroflex.flightservice.aircraft.dto.AircraftTypeRequest;
import com.aeroflex.flightservice.aircraft.dto.AircraftTypeResponse;
import com.aeroflex.flightservice.aircraft.service.AircraftTypeService;
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
@RequestMapping("/api/v1/aircraft-types")
@RequiredArgsConstructor
public class AircraftTypeController {

    private final AircraftTypeService aircraftTypeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AircraftTypeResponse>>
    createAircraftType(
            @Valid @RequestBody AircraftTypeRequest request) {

        AircraftTypeResponse response =
                aircraftTypeService.createAircraftType(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Aircraft type created successfully",
                        response
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AircraftTypeResponse>>
    getAircraftTypeById(
            @PathVariable UUID id) {

        AircraftTypeResponse response =
                aircraftTypeService.getAircraftTypeById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft type retrieved successfully",
                        response
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AircraftTypeResponse>>>
    getAircraftTypes(
            @RequestParam(required = false) String search,
            @PageableDefault(
                    size = 10,
                    sort = "model",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {

        Page<AircraftTypeResponse> response =
                aircraftTypeService.getAircraftTypes(
                        search,
                        pageable
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft types retrieved successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<AircraftTypeResponse>>
    updateAircraftType(
            @PathVariable UUID id,
            @Valid @RequestBody AircraftTypeRequest request) {

        AircraftTypeResponse response =
                aircraftTypeService.updateAircraftType(
                        id,
                        request
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft type updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>>
    deleteAircraftType(
            @PathVariable UUID id) {

        aircraftTypeService.deleteAircraftType(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Aircraft type deleted successfully"
                )
        );
    }
}
