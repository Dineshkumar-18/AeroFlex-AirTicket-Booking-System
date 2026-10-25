package com.aeroflex.flightscheduleservice.dto;

import com.aeroflex.common.enums.FlightStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleStatusUpdateRequest {

    @NotNull(message = "Flight status is required")
    private FlightStatus status;

    private LocalDateTime actualDepartureTime;
    private LocalDateTime actualArrivalTime;
    private String gate;
    private String terminal;
    private String reason;
}
