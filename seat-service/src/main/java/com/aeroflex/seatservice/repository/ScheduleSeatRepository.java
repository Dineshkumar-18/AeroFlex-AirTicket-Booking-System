package com.aeroflex.seatservice.repository;

import com.aeroflex.common.enums.SeatStatus;
import com.aeroflex.seatservice.entity.ScheduleSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, UUID> {

    List<ScheduleSeat> findByScheduleIdOrderBySeatRowAscSeatColumnAsc(UUID scheduleId);

    Optional<ScheduleSeat> findByScheduleIdAndSeatNumber(UUID scheduleId, String seatNumber);

    List<ScheduleSeat> findByScheduleIdAndSeatNumberIn(UUID scheduleId, List<String> seatNumbers);

    List<ScheduleSeat> findByScheduleIdAndStatus(UUID scheduleId, SeatStatus status);

    @Query("SELECT s FROM ScheduleSeat s WHERE s.status = 'LOCKED' AND s.lockExpiresAt < :now")
    List<ScheduleSeat> findExpiredLocks(@Param("now") LocalDateTime now);

    @Modifying
    @Query("UPDATE ScheduleSeat s SET s.status = 'AVAILABLE', s.lockedByUserId = null, s.lockToken = null, s.lockExpiresAt = null WHERE s.status = 'LOCKED' AND s.lockExpiresAt < :now")
    int releaseExpiredLocks(@Param("now") LocalDateTime now);

    boolean existsByScheduleId(UUID scheduleId);
}
