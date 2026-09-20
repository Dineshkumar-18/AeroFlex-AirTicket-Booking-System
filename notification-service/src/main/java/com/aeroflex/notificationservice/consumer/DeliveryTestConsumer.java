package com.aeroflex.notificationservice.consumer;

import com.aeroflex.common.constants.TopicConstants;
import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import com.aeroflex.notificationservice.service.DeliveryService;
import com.aeroflex.notificationservice.service.NotificationDeliveryDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryTestConsumer {

    private final NotificationDeliveryDispatcher dispatcher;
    private final DeliveryService deliveryService;

    @RetryableTopic(
            attempts = "4",
            backOff = @BackOff(delay = 30000,multiplier = 2.0),
            dltTopicSuffix = ".dlt"
    )
    @KafkaListener(
            topics = TopicConstants.NOTIFICATION_DELIVERY_TOPIC,
            groupId = "notification-delivery-test",
            containerFactory =
                    "notificationDeliveryKafkaListenerContainerFactory"
    )
    public void consume(NotificationDeliveryEvent event) {

        //dispatcher.dispatch(event);
        deliveryService.deliver(event);
    }
}
