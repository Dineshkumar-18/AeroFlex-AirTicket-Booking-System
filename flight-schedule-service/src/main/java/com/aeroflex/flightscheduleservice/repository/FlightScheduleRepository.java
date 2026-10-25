package com.aeroflex.flightscheduleservice.repository;

import com.aeroflex.flightscheduleservice.entity.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, UUID> {

    List<FlightSchedule> findByFlightNumber(String flightNumber);

    @Query("SELECT s FROM FlightSchedule s WHERE s.departureAirportCode = :origin AND s.arrivalAirportCode = :destination AND s.scheduledDepartureTime BETWEEN :startTime AND :endTime AND s.status <> 'CANCELLED'")
    List<FlightSchedule> searchSchedules(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT s FROM FlightSchedule s WHERE s.departureAirportCode = :origin AND s.arrivalAirportCode = :destination AND s.status <> 'CANCELLED'")
    List<FlightSchedule> findByOriginAndDestination(
            @Param("origin") String origin,
            @Param("destination") String destination
    );
}
