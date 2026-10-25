package com.aeroflex.seatservice.service;

import com.aeroflex.seatservice.dto.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface SeatService {
    List<ScheduleSeatResponse> getSeatMap(UUID scheduleId);
    SeatLockResponse lockSeats(SeatLockRequest request);
    void releaseSeats(SeatReleaseRequest request);
    void confirmSeats(SeatConfirmRequest request);
    List<ScheduleSeatResponse> initializeSeatLayout(UUID scheduleId, String aircraftModel, BigDecimal baseEconomyPrice);
}
