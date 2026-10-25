package com.aeroflex.bookingservice.dto;

import com.aeroflex.common.enums.Gender;
import com.aeroflex.common.enums.SeatClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private LocalDate dateOfBirth;

    private Gender gender;

    private String passportNumber;

    private String nationality;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Seat class is required")
    private SeatClass seatClass;
}
