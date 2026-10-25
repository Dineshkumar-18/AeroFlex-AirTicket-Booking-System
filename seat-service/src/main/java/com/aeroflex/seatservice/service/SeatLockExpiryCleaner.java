package com.aeroflex.seatservice.service;

import com.aeroflex.seatservice.repository.ScheduleSeatRepository;
import com.aeroflex.seatservice.repository.SeatLockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeatLockExpiryCleaner {

    private final ScheduleSeatRepository seatRepository;
    private final SeatLockRepository lockRepository;

    @Scheduled(fixedRate = 30000) // Runs every 30 seconds
    @Transactional
    public void cleanExpiredLocks() {
        LocalDateTime now = LocalDateTime.now();
        int releasedSeats = seatRepository.releaseExpiredLocks(now);
        int deactivatedLocks = lockRepository.deactivateExpiredLocks(now);

        if (releasedSeats > 0) {
            log.info("Auto-released {} expired seat locks (deactivated {} lock records)", releasedSeats, deactivatedLocks);
        }
    }
}
