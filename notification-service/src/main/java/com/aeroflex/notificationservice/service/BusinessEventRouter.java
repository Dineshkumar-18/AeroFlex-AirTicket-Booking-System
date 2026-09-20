package com.aeroflex.notificationservice.service;

import com.aeroflex.common.events.BusinessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessEventRouter {

    private final NotificationUserProfileService userProfileService;
    private final NotificationEventProcessor notificationEventProcessor;

    public void route(BusinessEvent<Object> event) {

        if ("USER_PROFILE_UPDATED".equals(event.eventType())) {

            userProfileService.process(event);

            return;
        }

        notificationEventProcessor.process(event);
    }
}
