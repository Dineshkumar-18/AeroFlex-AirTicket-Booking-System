package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import com.aeroflex.notificationservice.enums.ChannelDelivery;
import com.aeroflex.notificationservice.enums.DeliveryStatus;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.enums.NotificationDeliveryContext;
import com.aeroflex.notificationservice.model.NotificationDelivery;
import com.aeroflex.notificationservice.model.NotificationPolicy;
import com.aeroflex.notificationservice.repository.NotificationDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final NotificationDeliveryRepository repository;
    private final NotificationDeliveryDispatcher dispatcher;
    private final DeliveryFailureService deliveryFailureService;
    private final NotificationTemplateService notificationTemplateService;
    private final RecipientResolver recipientResolver;
    private final ChannelDeliveryFactory  deliveryFactory;
    private final NotificationPolicyEngine notificationPolicyEngine;

    @Transactional
    public void deliver(NotificationDeliveryEvent event) {

        NotificationDelivery delivery =
                repository.findByNotificationIdAndChannel(
                        event.notificationId(),
                        event.channel()
                ).orElseGet(() -> createDelivery(event));

        if (delivery.getStatus() == DeliveryStatus.SENT) {
            System.out.println(
                    "Delivery already completed: "
                            + delivery.getId()
            );
            return;
        }

        try {


            delivery.setStatus(
                    DeliveryStatus.PROCESSING
            );

            delivery.setAttemptCount(
                    delivery.getAttemptCount() + 1
            );

            repository.save(delivery);

            ChannelRecipient recipient =
                    recipientResolver.resolve(
                            event.userId(),
                            event.channel()
                    );

            NotificationPolicy policy =
                    notificationPolicyEngine.getPolicy(
                            event.templateCode(),
                            event.templateVersion(),
                            event.channel()
                    );

            String content =
                    notificationTemplateService.render(event);

            System.out.println("===== RENDERED TEMPLATE =====");
            System.out.println(content);
            System.out.println("=============================");

            // 3. Common context
            NotificationDeliveryContext context =
                    new NotificationDeliveryContext(
                            event.notificationId(),
                            UUID.fromString(event.eventId())
                    );


            // 4. Create channel-specific delivery
            ChannelDelivery channelDelivery =
                    deliveryFactory.create(
                            context,
                            event.channel(),
                            recipient,
                            policy.getSubject(),
                            content,
                            event.attachments()
                    );


            dispatcher.dispatch(event.channel(), channelDelivery);

            delivery.setStatus(
                    DeliveryStatus.SENT
            );

            delivery.setSentAt(
                    Instant.now()
            );

            repository.save(delivery);

        } catch (Exception ex) {

            deliveryFailureService.markFailed(
                    delivery.getId(),
                    ex.getMessage()
            );
            throw ex;
        }
    }

    private NotificationDelivery createDelivery(
            NotificationDeliveryEvent event) {

        NotificationDelivery delivery =
                new NotificationDelivery();

        delivery.setNotificationId(
                event.notificationId()
        );

        delivery.setChannel(
                event.channel()
        );

        delivery.setProvider(
                getProvider(event.channel())
        );

        delivery.setStatus(
                DeliveryStatus.PENDING
        );

        delivery.setAttemptCount(0);
        delivery.setCreatedAt(Instant.now());

        return repository.saveAndFlush(delivery);
    }

    private String getProvider(
            NotificationChannel channel) {

        return switch (channel) {
            case EMAIL -> "SMTP";
            case SMS -> "SMS_PROVIDER";
        };
    }
}
