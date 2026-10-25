package com.aeroflex.flightservice.aircraft.repository;

import com.aeroflex.flightservice.aircraft.entity.AircraftType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AircraftTypeRepository
        extends JpaRepository<AircraftType, UUID> {

    boolean existsByModelIgnoreCase(String model);

    boolean existsByModelIgnoreCaseAndIdNot(
            String model,
            UUID id
    );

    @Query("""
            SELECT a
            FROM AircraftType a
            WHERE LOWER(a.model) LIKE LOWER(CONCAT('%', :search, '%'))
               OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :search, '%'))
            """)
    Page<AircraftType> search(
            @Param("search") String search,
            Pageable pageable
    );

    Optional<AircraftType> findByIdAndIsActiveTrue(UUID id);


}
