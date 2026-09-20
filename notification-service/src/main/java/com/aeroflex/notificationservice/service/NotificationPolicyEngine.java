package com.aeroflex.notificationservice.service;

import com.aeroflex.common.exception.ResourceNotFoundException;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.model.NotificationPolicy;
import com.aeroflex.notificationservice.repository.NotificationPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationPolicyEngine
        implements PolicyEngine {

    private final NotificationPolicyRepository repository;

    @Override
    public List<NotificationPolicy> getPolicies(
            String eventType,
            String eventVersion) {

        return repository
                .findByEventTypeAndEventVersionAndEnabledTrue(
                        eventType,
                        eventVersion);
    }

    @Override
    public NotificationPolicy getPolicy(String templateCode, String version, NotificationChannel channel) {
        return repository.findByTemplateCodeAndTemplateVersionAndChannelAndEnabledTrue(templateCode,version,channel)
                .orElseThrow(()-> new ResourceNotFoundException("Notification Policy Not Found"));
    }

}
