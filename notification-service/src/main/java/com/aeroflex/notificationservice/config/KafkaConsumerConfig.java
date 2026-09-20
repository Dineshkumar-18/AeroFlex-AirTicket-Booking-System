package com.aeroflex.notificationservice.config;

import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, NotificationDeliveryEvent>
    notificationDeliveryConsumerFactory(
            KafkaProperties kafkaProperties) {

        Map<String, Object> props =
                new HashMap<>(
                        kafkaProperties.buildConsumerProperties()
                );

        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JacksonJsonDeserializer.class
        );

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>(
                        NotificationDeliveryEvent.class
                )
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String, NotificationDeliveryEvent>
    notificationDeliveryKafkaListenerContainerFactory(
            ConsumerFactory<String, NotificationDeliveryEvent>
                    consumerFactory) {

        var factory =
                new ConcurrentKafkaListenerContainerFactory<
                                        String, NotificationDeliveryEvent>();

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String,
            BusinessEvent<Object>
            > businessEventKafkaListenerContainerFactory(
            ConsumerFactory<String, BusinessEvent<Object>>
                    businessEventConsumerFactory) {

        ConcurrentKafkaListenerContainerFactory<
                String,
                BusinessEvent<Object>
                > factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                businessEventConsumerFactory
        );

        return factory;
    }

    @Bean
    public ConsumerFactory<String, BusinessEvent<Object>>
    businessEventConsumerFactory(
            KafkaProperties kafkaProperties) {

        Map<String, Object> props =
                new HashMap<>(
                        kafkaProperties.buildConsumerProperties()
                );

        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JacksonJsonDeserializer.class
        );

        props.put(
               JacksonJsonDeserializer.TRUSTED_PACKAGES,
                "com.aeroflex.common.events"
        );

        props.put(
                JacksonJsonDeserializer.VALUE_DEFAULT_TYPE,
                BusinessEvent.class.getName()
        );

        props.put(
                JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS,
                false
        );

        return new DefaultKafkaConsumerFactory<>(props);
    }
}
