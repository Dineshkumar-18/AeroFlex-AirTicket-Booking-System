package com.aeroflex.seatservice.repository;

import com.aeroflex.seatservice.entity.SeatLock;
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
public interface SeatLockRepository extends JpaRepository<SeatLock, UUID> {

    List<SeatLock> findByScheduleIdAndLockToken(UUID scheduleId, String lockToken);

    Optional<SeatLock> findByScheduleIdAndSeatNumberAndActiveTrue(UUID scheduleId, String seatNumber);

    @Modifying
    @Query("UPDATE SeatLock l SET l.active = false WHERE l.scheduleId = :scheduleId AND l.seatNumber IN :seatNumbers")
    void deactivateLocks(@Param("scheduleId") UUID scheduleId, @Param("seatNumbers") List<String> seatNumbers);

    @Modifying
    @Query("UPDATE SeatLock l SET l.active = false WHERE l.active = true AND l.expiresAt < :now")
    int deactivateExpiredLocks(@Param("now") LocalDateTime now);
}
