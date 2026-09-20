package com.aeroflex.identityservice.user.service;

import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.common.security.SecurityUtils;
import com.aeroflex.identityservice.auth.enums.OwnerProfileStatus;
import com.aeroflex.identityservice.user.dto.request.FlightOwnerProfileRequest;
import com.aeroflex.identityservice.user.dto.response.FlightOwnerProfileResponse;
import com.aeroflex.identityservice.user.entity.FlightOwnerProfile;
import com.aeroflex.identityservice.user.mapper.FlightOwnerProfileMapper;
import com.aeroflex.identityservice.user.repository.FlightOwnerProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FlightOwnerOnboardingServiceImpl
        implements FlightOwnerOnboardingService {

    private final FlightOwnerProfileRepository flightOwnerProfileRepository;
    private final FlightOwnerProfileMapper flightOwnerProfileMapper;

    @Override
    @Transactional
    public FlightOwnerProfileResponse submitProfile(
            FlightOwnerProfileRequest request) {

        UUID userId = SecurityUtils.getCurrentUserId();

        log.info("Submitting flight owner profile for userId={}", userId);

        FlightOwnerProfile profile =
                flightOwnerProfileRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Flight owner profile not found for user"
                                ));

        validateProfileSubmission(profile);

        flightOwnerProfileMapper.updateEntity(request, profile);

        profile.setOwnerProfileStatus(
                OwnerProfileStatus.PENDING_APPROVAL
        );

        profile.setSubmittedAt(LocalDateTime.now());

        profile.setApprovedAt(null);

        FlightOwnerProfile savedProfile =
                flightOwnerProfileRepository.save(profile);

        log.info(
                "Flight owner profile submitted successfully. userId={}, profileId={}",
                userId,
                savedProfile.getId()
        );

        return flightOwnerProfileMapper.toResponse(savedProfile);
    }

    @Override
    public FlightOwnerProfileResponse getProfile() {

        UUID userId = SecurityUtils.getCurrentUserId();

        FlightOwnerProfile profile =
                flightOwnerProfileRepository.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Flight owner profile not found"
                                ));

        return flightOwnerProfileMapper.toResponse(profile);
    }

    private void validateProfileSubmission(
            FlightOwnerProfile profile) {

        OwnerProfileStatus status =
                profile.getOwnerProfileStatus();

        if (status == OwnerProfileStatus.PENDING_APPROVAL) {
            throw new IllegalStateException(
                    "Flight owner profile is already pending approval"
            );
        }

        if (status == OwnerProfileStatus.APPROVED) {
            throw new IllegalStateException(
                    "Flight owner profile is already approved"
            );
        }
    }
}
