package com.aeroflex.bookingservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Schedule ID is required")
    private UUID scheduleId;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Departure airport is required")
    private String departureAirport;

    @NotBlank(message = "Arrival airport is required")
    private String arrivalAirport;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;

    @Builder.Default
    private String currency = "USD";

    @NotBlank(message = "Contact email is required")
    @Email(message = "Invalid email address")
    private String contactEmail;

    @NotBlank(message = "Contact phone is required")
    private String contactPhone;

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerRequest> passengers;
}
