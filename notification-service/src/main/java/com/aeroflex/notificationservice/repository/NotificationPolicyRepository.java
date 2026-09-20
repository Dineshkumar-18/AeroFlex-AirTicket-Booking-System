package com.aeroflex.notificationservice.repository;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.model.NotificationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationPolicyRepository
        extends JpaRepository<NotificationPolicy, UUID> {

    List<NotificationPolicy>
    findByEventTypeAndEventVersionAndEnabledTrue(
            String eventType,
            String eventVersion);

    Optional<NotificationPolicy> findByTemplateCodeAndTemplateVersionAndChannelAndEnabledTrue(
            String eventType,
            String eventVersion,
            NotificationChannel channel
    );
}