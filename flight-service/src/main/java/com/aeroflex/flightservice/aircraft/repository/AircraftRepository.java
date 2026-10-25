package com.aeroflex.flightservice.aircraft.repository;

import com.aeroflex.flightservice.aircraft.entity.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AircraftRepository
        extends JpaRepository<Aircraft, UUID> {

    boolean existsByRegistrationNumberIgnoreCase(
            String registrationNumber
    );

    boolean existsByRegistrationNumberIgnoreCaseAndIdNot(
            String registrationNumber,
            UUID id
    );

    @Query("""
            SELECT a
            FROM Aircraft a
            JOIN a.aircraftType at
            WHERE LOWER(a.registrationNumber)
                    LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(a.name)
                    LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(at.model)
                    LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<Aircraft> search(
            @Param("search") String search,
            Pageable pageable
    );

    //for flight-owner only active aircraft
    Page<Aircraft> findByAirlineIdAndIsActiveTrue(
            UUID airlineId,
            Pageable pageable
    );

    // for Admin
    Page<Aircraft> findByAirlineId(
            UUID airlineId,
            Pageable pageable
    );

    Optional<Aircraft> findByIdAndIsActiveTrue(UUID id);
}
