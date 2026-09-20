package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import com.aeroflex.notificationservice.enums.ChannelDelivery;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationDeliveryDispatcher {

    private final Map<NotificationChannel, NotificationChannelSender<? extends ChannelDelivery>>
            senders;

    public NotificationDeliveryDispatcher(
            List<NotificationChannelSender<? extends ChannelDelivery>> senders) {

        this.senders = senders.stream()
                .collect(Collectors.toMap(
                        NotificationChannelSender::getChannel,
                        Function.identity()
                ));
    }

    public void dispatch(
            NotificationChannel channel,
            ChannelDelivery delivery) {

        NotificationChannelSender<?> sender =
                senders.get(channel);

        if (sender == null) {
            throw new IllegalStateException(
                    "No sender configured for channel: "
                            + channel
            );
        }

        dispatchInternal(sender, delivery);
    }

    @SuppressWarnings("unchecked")
    private <T extends ChannelDelivery> void dispatchInternal(
            NotificationChannelSender<?> sender,
            ChannelDelivery delivery) {

        NotificationChannelSender<T> typedSender =
                (NotificationChannelSender<T>) sender;

        typedSender.send((T) delivery);
    }
}