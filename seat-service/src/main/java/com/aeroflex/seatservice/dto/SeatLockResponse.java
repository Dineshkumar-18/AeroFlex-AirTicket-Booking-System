package com.aeroflex.seatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatLockResponse {
    private UUID scheduleId;
    private List<String> lockedSeats;
    private String lockToken;
    private LocalDateTime expiresAt;
    private long lockDurationSeconds;
}
