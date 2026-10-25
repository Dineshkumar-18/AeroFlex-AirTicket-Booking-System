package com.aeroflex.flightservice.airport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirportResponse {
    private UUID id;
    private String code;
    private String icaoCode;
    private String name;
    private String city;
    private String country;
    private String timezone;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
