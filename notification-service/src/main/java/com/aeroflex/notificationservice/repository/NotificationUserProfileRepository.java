package com.aeroflex.notificationservice.repository;

import com.aeroflex.notificationservice.model.NotificationUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationUserProfileRepository
        extends JpaRepository<NotificationUserProfile, UUID> {

    Optional<NotificationUserProfile> findByUserId(String userId);
}
