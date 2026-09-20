package com.aeroflex.notificationservice.service;

import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.notificationservice.model.NotificationPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationEventProcessor {

    private final IdempotencyService idempotencyService;
    private final PolicyEngine policyEngine;
    private final NotificationService notificationService;

    @Transactional
    public void process(
            BusinessEvent<?> event) {

        if (idempotencyService.isProcessed(
                event.eventId())) {
           System.out.println("Event is already processed: " + event.eventId());
            return;
        }

        List<NotificationPolicy> policies =
                policyEngine.getPolicies(
                        event.eventType(),
                        event.eventVersion());

        for (NotificationPolicy policy : policies) {

            notificationService.create(
                    event,
                    policy);
        }

        System.out.println("NotificationEventProcessor processing completed");

        idempotencyService.markProcessed(
                event.eventId());
    }
}
