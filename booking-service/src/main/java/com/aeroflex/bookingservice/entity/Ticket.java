package com.aeroflex.bookingservice.entity;

import com.aeroflex.common.entity.BaseEntity;
import com.aeroflex.common.enums.SeatClass;
import com.aeroflex.common.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets", indexes = {
        @Index(name = "idx_ticket_number", columnList = "ticketNumber", unique = true),
        @Index(name = "idx_ticket_pnr", columnList = "pnr"),
        @Index(name = "idx_ticket_booking", columnList = "bookingId")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends BaseEntity {

    @Column(nullable = false)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID passengerId;

    @Column(nullable = false, unique = true, length = 30)
    private String ticketNumber; // e.g. "098-5432109876"

    @Column(nullable = false, length = 6)
    private String pnr;

    @Column(nullable = false, length = 100)
    private String passengerName;

    @Column(nullable = false, length = 20)
    private String flightNumber;

    @Column(nullable = false, length = 3)
    private String origin;

    @Column(nullable = false, length = 3)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false, length = 10)
    private String seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatClass seatClass;

    @Column(length = 20)
    private String gate;

    @Column(length = 20)
    private String terminal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TicketStatus status = TicketStatus.ISSUED;

    @Column(length = 500)
    private String qrCodeData;

    private LocalDateTime issuedAt;
}
