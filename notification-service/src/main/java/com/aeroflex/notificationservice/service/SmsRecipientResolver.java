package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.enums.SmsRecipient;
import com.aeroflex.notificationservice.model.NotificationUserProfile;
import org.springframework.stereotype.Component;

@Component
public class SmsRecipientResolver
        implements ChannelRecipientResolver<SmsRecipient> {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.SMS;
    }

    @Override
    public SmsRecipient resolve(
            NotificationUserProfile profile) {

        if (!profile.isSmsVerified()) {
            throw new IllegalStateException(
                    "SMS is not verified for user: "
                            + profile.getUserId()
            );
        }

        return new SmsRecipient(
                profile.getMobile()
        );
    }
}
