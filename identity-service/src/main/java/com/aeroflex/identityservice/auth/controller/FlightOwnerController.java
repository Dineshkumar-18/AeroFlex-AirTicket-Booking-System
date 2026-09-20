package com.aeroflex.identityservice.auth.controller;

import com.aeroflex.common.response.ApiResponse;
import com.aeroflex.identityservice.user.dto.request.FlightOwnerProfileRequest;
import com.aeroflex.identityservice.user.dto.response.FlightOwnerProfileResponse;
import com.aeroflex.identityservice.user.service.FlightOwnerOnboardingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account/auth/v1/flight-owners")
public class FlightOwnerController {

    private final FlightOwnerOnboardingService onboardingService;

    public FlightOwnerController(
            FlightOwnerOnboardingService onboardingService
    ) {
        this.onboardingService = onboardingService;
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<FlightOwnerProfileResponse>>
    completeProfile(
            @Valid @RequestBody
            FlightOwnerProfileRequest request
    ) {

        FlightOwnerProfileResponse response =
                onboardingService.submitProfile(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Flight owner profile submitted for approval",
                        response
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<FlightOwnerProfileResponse>>
    getMyProfile() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Flight owner profile retrieved",
                        onboardingService.getProfile()
                )
        );
    }
}
