package com.aeroflex.common.events;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, BusinessEvent<?>> kafkaTemplate;

    @Override
    public void publish(String topic, BusinessEvent<?> event) {

        kafkaTemplate.send(
                topic,
                event.eventId(),
                event
        );
    }
}
