package com.aeroflex.identityservice.user.repository;

import com.aeroflex.identityservice.user.entity.FlightOwnerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FlightOwnerProfileRepository
        extends JpaRepository<FlightOwnerProfile, UUID> {

    boolean existsByUser_Id(UUID userId);
}
