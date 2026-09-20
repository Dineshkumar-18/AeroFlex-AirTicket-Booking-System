package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import com.aeroflex.notificationservice.enums.ChannelDelivery;
import com.aeroflex.notificationservice.enums.NotificationChannel;

public interface NotificationChannelSender<
        T extends ChannelDelivery> {

    NotificationChannel getChannel();

    void send(T delivery);
}
