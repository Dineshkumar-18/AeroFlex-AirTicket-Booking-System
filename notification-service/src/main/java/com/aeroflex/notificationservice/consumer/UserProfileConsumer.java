package com.aeroflex.notificationservice.consumer;

import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.notificationservice.service.NotificationUserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileConsumer {

    private final NotificationUserProfileService notificationUserProfileService;

    @KafkaListener(
            topics = "user-profile-events",
            groupId = "notification-user-profile",
            containerFactory = "businessEventKafkaListenerContainerFactory"
    )
    public void consume(
            BusinessEvent<Object> event) {

        notificationUserProfileService.process(event);
    }
}
