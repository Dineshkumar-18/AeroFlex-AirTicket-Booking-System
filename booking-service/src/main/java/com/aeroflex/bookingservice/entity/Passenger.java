package com.aeroflex.bookingservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.Gender;
import com.aeroflex.common.enums.SeatClass;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(length = 30)
    private String passportNumber;

    @Column(length = 50)
    private String nationality;

    @Column(nullable = false, length = 10)
    private String seatNumber; // e.g. "12A"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatClass seatClass;

    @Column(length = 30)
    private String ticketNumber; // e.g. "098-1234567890"
}
