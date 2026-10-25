package com.aeroflex.flightservice.flight.repository;

import com.aeroflex.flightservice.flight.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {

    @Query("SELECT f FROM Flight f JOIN FETCH f.airline JOIN FETCH f.departureAirport JOIN FETCH f.arrivalAirport WHERE f.flightNumber = :flightNumber")
    Optional<Flight> findByFlightNumber(@Param("flightNumber") String flightNumber);

    boolean existsByFlightNumber(String flightNumber);

    @Query(value = "SELECT f FROM Flight f JOIN FETCH f.airline JOIN FETCH f.departureAirport JOIN FETCH f.arrivalAirport WHERE f.departureAirport.code = :origin AND f.arrivalAirport.code = :destination AND f.active = true",
           countQuery = "SELECT COUNT(f) FROM Flight f WHERE f.departureAirport.code = :origin AND f.arrivalAirport.code = :destination AND f.active = true")
    Page<Flight> findByOriginAndDestination(@Param("origin") String origin, @Param("destination") String destination, Pageable pageable);

    @Query(value = "SELECT f FROM Flight f JOIN FETCH f.airline JOIN FETCH f.departureAirport JOIN FETCH f.arrivalAirport",
           countQuery = "SELECT COUNT(f) FROM Flight f")
    Page<Flight> findAllWithDetails(Pageable pageable);
}
