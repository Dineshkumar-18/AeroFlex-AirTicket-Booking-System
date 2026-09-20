package com.aeroflex.common.events;

public interface EventPublisher {

    void publish(String topic, BusinessEvent<?> event);
}
