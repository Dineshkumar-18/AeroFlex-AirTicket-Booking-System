package com.aeroflex.flightservice.airline.dto;

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
public class AirlineRequest {

    @NotBlank(message = "Airline code is required")
    @Size(min = 2, max = 3, message = "Airline code must be 2 or 3 characters")
    private String code;

    @NotBlank(message = "Airline name is required")
    private String name;

    private String country;

    private String logoUrl;

    @Builder.Default
    private boolean active = true;
}
