package com.aeroflex.notificationservice.service;

import com.aeroflex.common.constants.TopicConstants;
import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.notificationservice.builder.TemplateDataBuilder;
import com.aeroflex.notificationservice.dto.NotificationAction;
import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import com.aeroflex.notificationservice.enums.NotificationStatus;
import com.aeroflex.notificationservice.model.Notification;
import com.aeroflex.notificationservice.model.NotificationPolicy;
import com.aeroflex.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final KafkaTemplate<String, NotificationDeliveryEvent> kafkaTemplate;
    private final TemplateDataBuilder templateDataBuilder;

    public Notification create(
            BusinessEvent<?> event,
            NotificationPolicy policy) {

        Notification notification = new Notification();

        notification.setEventId(event.eventId());
        notification.setEventType(event.eventType());
        notification.setEventVersion(event.eventVersion());
        notification.setUserId(event.userId());
        notification.setChannel(policy.getChannel());
        notification.setTemplateCode(policy.getTemplateCode());
        notification.setTemplateVersion(policy.getTemplateVersion());

        notification.setStatus(NotificationStatus.PENDING);

        Notification saved = repository.save(notification);

        NotificationDeliveryEvent deliveryEvent =
                new NotificationDeliveryEvent(
                        saved.getId(),
                        saved.getEventId(),
                        saved.getUserId(),
                        saved.getChannel(),
                        saved.getTemplateCode(),
                        saved.getTemplateVersion(),
                        templateDataBuilder.build(event),
                        event.attachments()
                );

        kafkaTemplate.send(
                TopicConstants.NOTIFICATION_DELIVERY_TOPIC,
                saved.getId().toString(),
                deliveryEvent
        );

        return saved;
    }
}