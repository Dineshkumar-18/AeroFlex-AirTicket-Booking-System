package com.aeroflex.notificationservice.consumer;

import com.aeroflex.common.constants.GroupIdConstants;
import com.aeroflex.common.constants.TopicConstants;
import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.common.events.booking.BookingConfirmedPayload;
import com.aeroflex.notificationservice.service.IdempotencyService;
import com.aeroflex.notificationservice.service.NotificationEventProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.internals.Topic;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class BusinessEventConsumer {

    private final NotificationEventProcessor processor;

    private final ObjectMapper mapper;

    private final IdempotencyService  idempotencyService;

    @KafkaListener(
            topics = TopicConstants.BUSINESS_EVENT_TOPIC,
            groupId = GroupIdConstants.NOTIFICATION_GROUP,
            containerFactory = "businessEventKafkaListenerContainerFactory"
    )
    public void consume(
            BusinessEvent<Object> event) {

        System.out.println(
                "Received event: " + event.eventType()
        );

        processor.process(event);
    }

}