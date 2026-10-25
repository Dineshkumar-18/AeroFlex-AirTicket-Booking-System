package com.aeroflex.seatservice.dto;

import com.aeroflex.common.enums.SeatClass;
import com.aeroflex.common.enums.SeatStatus;
import com.aeroflex.common.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSeatResponse {
    private UUID id;
    private UUID scheduleId;
    private String seatNumber;
    private int seatRow;
    private String seatColumn;
    private SeatClass seatClass;
    private SeatType seatType;
    private BigDecimal price;
    private SeatStatus status;
    private LocalDateTime lockExpiresAt;
}
