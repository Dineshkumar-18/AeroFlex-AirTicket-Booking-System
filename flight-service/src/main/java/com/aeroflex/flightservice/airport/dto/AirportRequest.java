package com.aeroflex.flightservice.airport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportRequest {

    @NotBlank(message = "Airport IATA code is required")
    @Size(min = 3, max = 3, message = "Airport IATA code must be exactly 3 characters")
    private String code;

    @Size(min = 4, max = 4, message = "ICAO code must be 4 characters")
    private String icaoCode;

    @NotBlank(message = "Airport name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    private String timezone;

    @Builder.Default
    private boolean active = true;
}
