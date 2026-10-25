package com.aeroflex.seatservice.service;

import com.aeroflex.common.enums.SeatClass;
import com.aeroflex.common.enums.SeatStatus;
import com.aeroflex.common.enums.SeatType;
import com.aeroflex.common.exception.BusinessException;
import com.aeroflex.common.exception.ConflictException;
import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.seatservice.dto.*;
import com.aeroflex.seatservice.entity.ScheduleSeat;
import com.aeroflex.seatservice.entity.SeatLock;
import com.aeroflex.seatservice.repository.ScheduleSeatRepository;
import com.aeroflex.seatservice.repository.SeatLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final ScheduleSeatRepository seatRepository;
    private final SeatLockRepository lockRepository;

    private static final int LOCK_DURATION_MINUTES = 10;

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleSeatResponse> getSeatMap(UUID scheduleId) {
        List<ScheduleSeat> seats = seatRepository.findByScheduleIdOrderBySeatRowAscSeatColumnAsc(scheduleId);
        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("No seat layout found for schedule id: " + scheduleId);
        }
        return seats.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SeatLockResponse lockSeats(SeatLockRequest request) {
        List<ScheduleSeat> seats = seatRepository.findByScheduleIdAndSeatNumberIn(
                request.getScheduleId(), request.getSeatNumbers()
        );

        if (seats.size() != request.getSeatNumbers().size()) {
            throw new ResourceNotFoundException("One or more specified seats do not exist in this schedule");
        }

        LocalDateTime now = LocalDateTime.now();

        // Check if any seat is already booked or actively locked by someone else
        for (ScheduleSeat seat : seats) {
            if (seat.getStatus() == SeatStatus.BOOKED) {
                throw new ConflictException("Seat " + seat.getSeatNumber() + " is already booked");
            }
            if (seat.getStatus() == SeatStatus.BLOCKED) {
                throw new BusinessException("Seat " + seat.getSeatNumber() + " is blocked and cannot be selected");
            }
            if (seat.getStatus() == SeatStatus.LOCKED && seat.getLockExpiresAt() != null && seat.getLockExpiresAt().isAfter(now)) {
                if (!request.getUserId().equals(seat.getLockedByUserId())) {
                    throw new ConflictException("Seat " + seat.getSeatNumber() + " is currently locked by another customer");
                }
            }
        }

        String lockToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = now.plusMinutes(LOCK_DURATION_MINUTES);

        for (ScheduleSeat seat : seats) {
            seat.setStatus(SeatStatus.LOCKED);
            seat.setLockedByUserId(request.getUserId());
            seat.setLockToken(lockToken);
            seat.setLockExpiresAt(expiresAt);
            seatRepository.save(seat);

            SeatLock lock = SeatLock.builder()
                    .scheduleId(request.getScheduleId())
                    .seatNumber(seat.getSeatNumber())
                    .userId(request.getUserId())
                    .lockToken(lockToken)
                    .expiresAt(expiresAt)
                    .active(true)
                    .build();
            lockRepository.save(lock);
        }

        log.info("Locked {} seats for user {} on schedule {}", seats.size(), request.getUserId(), request.getScheduleId());

        return SeatLockResponse.builder()
                .scheduleId(request.getScheduleId())
                .lockedSeats(request.getSeatNumbers())
                .lockToken(lockToken)
                .expiresAt(expiresAt)
                .lockDurationSeconds(LOCK_DURATION_MINUTES * 60L)
                .build();
    }

    @Override
    @Transactional
    public void releaseSeats(SeatReleaseRequest request) {
        List<ScheduleSeat> seats = seatRepository.findByScheduleIdAndSeatNumberIn(
                request.getScheduleId(), request.getSeatNumbers()
        );

        for (ScheduleSeat seat : seats) {
            if (seat.getStatus() == SeatStatus.LOCKED) {
                seat.setStatus(SeatStatus.AVAILABLE);
                seat.setLockedByUserId(null);
                seat.setLockToken(null);
                seat.setLockExpiresAt(null);
                seatRepository.save(seat);
            }
        }

        lockRepository.deactivateLocks(request.getScheduleId(), request.getSeatNumbers());
        log.info("Released {} seats on schedule {}", request.getSeatNumbers().size(), request.getScheduleId());
    }

    @Override
    @Transactional
    public void confirmSeats(SeatConfirmRequest request) {
        List<ScheduleSeat> seats = seatRepository.findByScheduleIdAndSeatNumberIn(
                request.getScheduleId(), request.getSeatNumbers()
        );

        if (seats.size() != request.getSeatNumbers().size()) {
            throw new ResourceNotFoundException("One or more specified seats do not exist");
        }

        for (ScheduleSeat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setBookingId(request.getBookingId());
            seat.setLockedByUserId(null);
            seat.setLockToken(null);
            seat.setLockExpiresAt(null);
            seatRepository.save(seat);
        }

        lockRepository.deactivateLocks(request.getScheduleId(), request.getSeatNumbers());
        log.info("Confirmed {} seats for booking {} on schedule {}", seats.size(), request.getBookingId(), request.getScheduleId());
    }

    @Override
    @Transactional
    public List<ScheduleSeatResponse> initializeSeatLayout(UUID scheduleId, String aircraftModel, BigDecimal baseEconomyPrice) {
        if (seatRepository.existsByScheduleId(scheduleId)) {
            throw new ConflictException("Seat layout already initialized for schedule id: " + scheduleId);
        }

        List<ScheduleSeat> seatList = new ArrayList<>();
        BigDecimal ecoPrice = baseEconomyPrice != null ? baseEconomyPrice : new BigDecimal("100.00");
        BigDecimal premPrice = ecoPrice.multiply(new BigDecimal("1.5"));
        BigDecimal bizPrice = ecoPrice.multiply(new BigDecimal("3.0"));
        BigDecimal firstPrice = ecoPrice.multiply(new BigDecimal("6.0"));

        if (aircraftModel != null && aircraftModel.contains("A320")) {
            // A320 layout: 30 rows of 3-3 (A, B, C, D, E, F)
            String[] cols = {"A", "B", "C", "D", "E", "F"};
            for (int row = 1; row <= 30; row++) {
                SeatClass seatClass = row <= 4 ? SeatClass.BUSINESS : (row <= 8 ? SeatClass.PREMIUM_ECONOMY : SeatClass.ECONOMY);
                BigDecimal price = row <= 4 ? bizPrice : (row <= 8 ? premPrice : ecoPrice);

                for (String col : cols) {
                    SeatType type = (col.equals("A") || col.equals("F")) ? SeatType.WINDOW :
                            ((col.equals("C") || col.equals("D")) ? SeatType.AISLE : SeatType.MIDDLE);
                    if (row == 12 || row == 13) {
                        type = SeatType.EMERGENCY_EXIT;
                    }

                    seatList.add(ScheduleSeat.builder()
                            .scheduleId(scheduleId)
                            .seatNumber(row + col)
                            .seatRow(row)
                            .seatColumn(col)
                            .seatClass(seatClass)
                            .seatType(type)
                            .price(price)
                            .status(SeatStatus.AVAILABLE)
                            .build());
                }
            }
        } else {
            // Wide-body (e.g. B777 / A350): Rows 1-2 First Class (1-2-1), Rows 3-8 Business (2-3-2), Rows 9-14 Premium Eco (2-4-2), Rows 15-40 Economy (3-4-3)
            // First Class: Rows 1-2
            String[] firstCols = {"A", "D", "G", "K"};
            for (int row = 1; row <= 2; row++) {
                for (String col : firstCols) {
                    seatList.add(ScheduleSeat.builder()
                            .scheduleId(scheduleId)
                            .seatNumber(row + col)
                            .seatRow(row)
                            .seatColumn(col)
                            .seatClass(SeatClass.FIRST_CLASS)
                            .seatType(col.equals("A") || col.equals("K") ? SeatType.WINDOW : SeatType.AISLE)
                            .price(firstPrice)
                            .status(SeatStatus.AVAILABLE)
                            .build());
                }
            }

            // Business Class: Rows 3-7 (A, B, D, E, F, J, K)
            String[] bizCols = {"A", "B", "D", "E", "F", "J", "K"};
            for (int row = 3; row <= 7; row++) {
                for (String col : bizCols) {
                    SeatType type = (col.equals("A") || col.equals("K")) ? SeatType.WINDOW :
                            ((col.equals("B") || col.equals("D") || col.equals("F") || col.equals("J")) ? SeatType.AISLE : SeatType.MIDDLE);
                    seatList.add(ScheduleSeat.builder()
                            .scheduleId(scheduleId)
                            .seatNumber(row + col)
                            .seatRow(row)
                            .seatColumn(col)
                            .seatClass(SeatClass.BUSINESS)
                            .seatType(type)
                            .price(bizPrice)
                            .status(SeatStatus.AVAILABLE)
                            .build());
                }
            }

            // Premium Economy: Rows 8-12 (A, B, C, D, E, F, G, H)
            String[] premCols = {"A", "B", "C", "D", "E", "F", "G", "H"};
            for (int row = 8; row <= 12; row++) {
                for (String col : premCols) {
                    SeatType type = (col.equals("A") || col.equals("H")) ? SeatType.WINDOW :
                            ((col.equals("C") || col.equals("D") || col.equals("E") || col.equals("F")) ? SeatType.AISLE : SeatType.MIDDLE);
                    seatList.add(ScheduleSeat.builder()
                            .scheduleId(scheduleId)
                            .seatNumber(row + col)
                            .seatRow(row)
                            .seatColumn(col)
                            .seatClass(SeatClass.PREMIUM_ECONOMY)
                            .seatType(type)
                            .price(premPrice)
                            .status(SeatStatus.AVAILABLE)
                            .build());
                }
            }

            // Economy: Rows 14-38 (A, B, C, D, E, F, G, H, J, K)
            String[] ecoCols = {"A", "B", "C", "D", "E", "F", "G", "H", "J", "K"};
            for (int row = 14; row <= 38; row++) {
                for (String col : ecoCols) {
                    SeatType type = (col.equals("A") || col.equals("K")) ? SeatType.WINDOW :
                            ((col.equals("C") || col.equals("D") || col.equals("G") || col.equals("H")) ? SeatType.AISLE : SeatType.MIDDLE);
                    if (row == 20 || row == 21) {
                        type = SeatType.EMERGENCY_EXIT;
                    }
                    seatList.add(ScheduleSeat.builder()
                            .scheduleId(scheduleId)
                            .seatNumber(row + col)
                            .seatRow(row)
                            .seatColumn(col)
                            .seatClass(SeatClass.ECONOMY)
                            .seatType(type)
                            .price(ecoPrice)
                            .status(SeatStatus.AVAILABLE)
                            .build());
                }
            }
        }

        List<ScheduleSeat> saved = seatRepository.saveAll(seatList);
        log.info("Initialized {} seats for schedule {}", saved.size(), scheduleId);

        return saved.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private ScheduleSeatResponse mapToResponse(ScheduleSeat seat) {
        return ScheduleSeatResponse.builder()
                .id(seat.getId())
                .scheduleId(seat.getScheduleId())
                .seatNumber(seat.getSeatNumber())
                .seatRow(seat.getSeatRow())
                .seatColumn(seat.getSeatColumn())
                .seatClass(seat.getSeatClass())
                .seatType(seat.getSeatType())
                .price(seat.getPrice())
                .status(seat.getStatus())
                .lockExpiresAt(seat.getLockExpiresAt())
                .build();
    }
}
