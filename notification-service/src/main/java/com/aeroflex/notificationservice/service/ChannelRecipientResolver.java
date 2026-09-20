package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.model.NotificationUserProfile;

public interface ChannelRecipientResolver<T extends ChannelRecipient> {

    NotificationChannel channel();

    T resolve(NotificationUserProfile profile);
}
