package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationAction;
import com.aeroflex.notificationservice.repository.NotificationPolicyRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class CachedNotificationPolicy
        implements NotificationPolicy {
    @Override
    public List<NotificationAction> getActions(String eventType) {
        return List.of();
    }

//    private final NotificationPolicyRepository repository;
//
//    private final Cache<
//                String,
//                List<NotificationAction>
//                > cache;
//
//    public CachedNotificationPolicy(
//            NotificationPolicyRepository repository) {
//
//        this.repository = repository;
//
//        this.cache = Caffeine.newBuilder()
//                .maximumSize(1_000)
//                .expireAfterWrite(
//                        Duration.ofMinutes(10)
//                )
//                .build();
//    }
//
//    @Override
//    public List<NotificationAction> getActions(
//            String eventType) {
//
//        return cache.get(
//                eventType,
//                this::loadActions
//        );
//    }
//
//    private List<NotificationAction> loadActions(
//            String eventType) {
//
//        return repository
//                .findByEventTypeAndEnabledTrueOrderByPriorityDesc(
//                        eventType
//                )
//                .stream()
//                .map(entity ->
//                        new NotificationAction(
//                                entity.getChannel(),
//                                entity.getTemplateCode()
//                        )
//                )
//                .toList();
//    }
}
