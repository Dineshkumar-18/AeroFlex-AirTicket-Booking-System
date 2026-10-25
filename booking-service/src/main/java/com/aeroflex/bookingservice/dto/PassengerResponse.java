package com.aeroflex.bookingservice.dto;

import com.aeroflex.common.enums.Gender;
import com.aeroflex.common.enums.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String passportNumber;
    private String nationality;
    private String seatNumber;
    private SeatClass seatClass;
    private String ticketNumber;
}
