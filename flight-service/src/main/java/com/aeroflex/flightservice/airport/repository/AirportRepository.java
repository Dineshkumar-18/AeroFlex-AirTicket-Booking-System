package com.aeroflex.flightservice.airport.repository;

import com.aeroflex.flightservice.airport.entity.Airport;
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
public interface AirportRepository extends JpaRepository<Airport, UUID> {
    Optional<Airport> findByCode(String code);
    boolean existsByCode(String code);
    List<Airport> findByCountryIgnoreCase(String country);
    List<Airport> findByCityIgnoreCase(String city);
    List<Airport> findByActiveTrue();

    @Query("""
    SELECT a FROM Airport a
    WHERE active = true AND (
    LOWER(a.code) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(a.city) LIKE LOWER(CONCAT('%', :search, '%'))
       OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')))
""")
    Page<Airport> searchAirports(
            @Param("search") String search,
            Pageable pageable
    );
}
