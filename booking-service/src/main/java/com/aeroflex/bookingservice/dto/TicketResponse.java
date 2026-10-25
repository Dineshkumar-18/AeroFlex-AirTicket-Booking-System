package com.aeroflex.bookingservice.dto;

import com.aeroflex.common.enums.SeatClass;
import com.aeroflex.common.enums.TicketStatus;
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
public class TicketResponse {
    private UUID id;
    private UUID bookingId;
    private UUID passengerId;
    private String ticketNumber;
    private String pnr;
    private String passengerName;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String seatNumber;
    private SeatClass seatClass;
    private String gate;
    private String terminal;
    private TicketStatus status;
    private String qrCodeData;
    private LocalDateTime issuedAt;
}
