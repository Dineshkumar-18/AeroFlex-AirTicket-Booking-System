package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationAction;
import com.aeroflex.notificationservice.repository.NotificationPolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseNotificationPolicy
        implements NotificationPolicy {
    @Override
    public List<NotificationAction> getActions(String eventType) {
        return List.of();
    }

//    private final NotificationPolicyRepository repository;
//
//    public DatabaseNotificationPolicy(
//            NotificationPolicyRepository repository) {
//
//        this.repository = repository;
//    }
//
//    @Override
//    public List<NotificationAction> getActions(
//            String eventType) {
//
//        return repository
//                .findByEventTypeAndEnabledTrueOrderByPriorityDesc(
//                        eventType
//                )
//                .stream()
//                .map(this::toAction)
//                .toList();
//    }
//
//    private NotificationAction toAction(
//            NotificationPolicyEntity entity) {
//
//        return new NotificationAction(
//                entity.getChannel(),
//                entity.getTemplateCode()
//        );
//    }
}
