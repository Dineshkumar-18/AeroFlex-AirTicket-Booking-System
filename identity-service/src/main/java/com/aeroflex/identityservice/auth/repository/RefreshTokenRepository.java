package com.aeroflex.identityservice.auth.repository;

import com.aeroflex.identityservice.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void deleteAllByUser_Id(UUID userId);

    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);

}
