package com.aeroflex.flightservice.airline.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AirlineDto(
        UUID airlineId,
        String airlineName,
        String iataCode,
        String headquarters,
        String country,
        String contactNumber,
        String email,
        Integer foundedYear,
        String websiteUrl,
        String airlineLogo,
        BigDecimal commissionPercentage,
        UUID ownerId
) {
}
