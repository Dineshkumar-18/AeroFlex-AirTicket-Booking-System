package com.aeroflex.flightservice.airline.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UpdateAirlineRequest(

        @NotBlank
        @Size(max = 100)
        String airlineName,

        @NotBlank
        @Size(max = 2)
        String iataCode,

        @Size(max = 50)
        String headquarters,

        @NotBlank
        String country,

        @NotBlank
        String contactNumber,

        @NotBlank
        @Email
        String email,

        @NotNull
        Integer foundedYear,

        @NotBlank
        String websiteUrl,

        @NotBlank
        String airlineLogo,

        @DecimalMin("0.0")
        @DecimalMax("100.0")
        BigDecimal commissionPercentage
) {
}
