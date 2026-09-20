package com.aeroflex.identityservice.mfa.repository;

import com.aeroflex.identityservice.mfa.entity.UserMfa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserMfaRepository
        extends JpaRepository<UserMfa, UUID> {

    Optional<UserMfa> findByUserId(UUID userId);
}
