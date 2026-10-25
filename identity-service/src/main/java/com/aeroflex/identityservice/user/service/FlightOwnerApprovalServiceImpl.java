package com.aeroflex.identityservice.user.service;

import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.identityservice.auth.enums.OwnerProfileStatus;
import com.aeroflex.identityservice.user.entity.FlightOwnerProfile;
import com.aeroflex.identityservice.user.repository.FlightOwnerProfileRepository;
import com.aeroflex.common.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FlightOwnerApprovalServiceImpl
        implements FlightOwnerApprovalService {

    private final FlightOwnerProfileRepository profileRepository;

    public FlightOwnerApprovalServiceImpl(
            FlightOwnerProfileRepository profileRepository
    ) {
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional
    public void approve(
            UUID userId,
            String remarks
    ) {

        FlightOwnerProfile profile =
                getProfile(userId);

        if (profile.getOwnerProfileStatus()
                != OwnerProfileStatus.PENDING_APPROVAL) {

            throw new BadRequestException(
                    "Flight owner profile is not pending approval"
            );
        }

        profile.setOwnerProfileStatus(
                OwnerProfileStatus.APPROVED
        );

        profile.setAdminRemarks(remarks);

        profile.setApprovedAt(
                LocalDateTime.now()
        );

        profileRepository.save(profile);
    }

    @Override
    @Transactional
    public void reject(
            UUID userId,
            String remarks
    ) {

        FlightOwnerProfile profile =
                getProfile(userId);

        if (profile.getOwnerProfileStatus()
                != OwnerProfileStatus.PENDING_APPROVAL) {

            throw new BadRequestException(
                    "Flight owner profile is not pending approval"
            );
        }

        profile.setOwnerProfileStatus(
                OwnerProfileStatus.REJECTED
        );

        profile.setAdminRemarks(remarks);

        profileRepository.save(profile);
    }

    private FlightOwnerProfile getProfile(
            UUID userId
    ) {

        return profileRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Flight owner profile not found"
                        )
                );
    }
}
