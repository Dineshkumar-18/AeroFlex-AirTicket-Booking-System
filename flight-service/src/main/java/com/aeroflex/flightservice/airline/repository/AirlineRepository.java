package com.aeroflex.flightservice.airline.repository;

import com.aeroflex.flightservice.airline.entity.Airline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, UUID> {

    Optional<Airline> findByIdAndOwnerId(
            UUID airlineId,
            UUID ownerId
    );

    boolean existsByIdAndOwnerId(UUID airlineId, UUID ownerId);

    Page<Airline> findAllByOwnerId(
            UUID ownerId,
            Pageable pageable
    );

    Optional<Airline> findByIdIsActiveTrue(
            UUID airlineId);

    @Query("""
        SELECT a
        FROM Airline a
        WHERE LOWER(a.airlineName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.iataCode) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(a.country) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Airline> search(
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
        SELECT a
        FROM Airline a
        WHERE a.ownerId = :ownerId
          AND (
                LOWER(a.airlineName) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(a.iataCode) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(a.country) LIKE LOWER(CONCAT('%', :search, '%'))
          )
    """)
    Page<Airline> searchByOwner(
            @Param("ownerId") UUID ownerId,
            @Param("search") String search,
            Pageable pageable
    );
}
