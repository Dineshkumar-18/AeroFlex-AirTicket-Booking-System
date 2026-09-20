package com.aeroflex.identityservice.user.service;

import com.aeroflex.identityservice.user.dto.request.FlightOwnerProfileRequest;
import com.aeroflex.identityservice.user.dto.response.FlightOwnerProfileResponse;

public interface FlightOwnerOnboardingService {

    FlightOwnerProfileResponse submitProfile(
            FlightOwnerProfileRequest request
    );

    FlightOwnerProfileResponse getProfile();
}
