package com.aeroflex.identityservice.user.dto.response;

import com.aeroflex.common.constants.ApprovalStatus;
import com.aeroflex.identityservice.auth.enums.OwnerProfileStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record FlightOwnerProfileResponse(

        UUID id,

        String companyName,

        String companyRegistrationNumber,

        String companyPhoneNumber,

        String companyEmail,

        String operatingLicenseNumber,

        LocalDate joinedDate,

        Integer totalFlightsManaged,

        String supportContact,

        String profilePictureUrl,

        ApprovalStatus approvalStatus,

        OwnerProfileStatus ownerProfileStatus,

        String adminRemarks,

        LocalDateTime submittedAt,

        LocalDateTime approvedAt
) {
}