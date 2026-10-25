package com.aeroflex.bookingservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_booking_pnr", columnList = "pnr", unique = true),
        @Index(name = "idx_booking_user", columnList = "userId"),
        @Index(name = "idx_booking_schedule", columnList = "scheduleId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends BaseEntity {

    @Column(nullable = false, unique = true, length = 6)
    private String pnr; // 6-character unique PNR, e.g. "AF78K2"

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID scheduleId;

    @Column(nullable = false, length = 20)
    private String flightNumber;

    @Column(nullable = false, length = 3)
    private String departureAirport;

    @Column(nullable = false, length = 3)
    private String arrivalAirport;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    @Builder.Default
    private BookingStatus status = BookingStatus.INITIATED;

    @Column(nullable = false, length = 150)
    private String contactEmail;

    @Column(nullable = false, length = 25)
    private String contactPhone;

    private UUID paymentId;

    @Column(length = 255)
    private String cancellationReason;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Passenger> passengers = new ArrayList<>();
}
