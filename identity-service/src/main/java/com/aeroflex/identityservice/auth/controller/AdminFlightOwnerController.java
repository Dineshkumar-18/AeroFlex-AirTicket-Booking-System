package com.aeroflex.identityservice.auth.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.identityservice.user.dto.request.ApprovalRequest;
import com.aeroflex.identityservice.user.service.FlightOwnerApprovalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/acount/auth/admin/flight-owners")
public class AdminFlightOwnerController {

    private final FlightOwnerApprovalService approvalService;

    public AdminFlightOwnerController(
            FlightOwnerApprovalService approvalService
    ) {
        this.approvalService = approvalService;
    }

    @PatchMapping("/{userId}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(
            @PathVariable UUID userId,
            @Valid @RequestBody ApprovalRequest request
    ) {

        approvalService.approve(
                userId,
                request.getRemarks()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Flight owner approved",
                        null
                )
        );
    }

    @PatchMapping("/{userId}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(
            @PathVariable UUID userId,
            @Valid @RequestBody ApprovalRequest request
    ) {

        approvalService.reject(
                userId,
                request.getRemarks()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Flight owner rejected",
                        null
                )
        );
    }
}