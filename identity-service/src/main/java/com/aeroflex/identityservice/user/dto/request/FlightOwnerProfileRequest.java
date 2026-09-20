package com.aeroflex.identityservice.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FlightOwnerProfileRequest(

        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name must not exceed 150 characters")
        String companyName,

        @NotBlank(message = "Company registration number is required")
        @Size(max = 100, message = "Company registration number must not exceed 100 characters")
        String companyRegistrationNumber,

        @Size(max = 20, message = "Company phone number must not exceed 20 characters")
        String companyPhoneNumber,

        @Email(message = "Invalid company email")
        @Size(max = 150, message = "Company email must not exceed 150 characters")
        String companyEmail,

        @NotBlank(message = "Operating license number is required")
        @Size(max = 100, message = "Operating license number must not exceed 100 characters")
        String operatingLicenseNumber,

        @Size(max = 100, message = "Support contact must not exceed 100 characters")
        String supportContact

) {}
