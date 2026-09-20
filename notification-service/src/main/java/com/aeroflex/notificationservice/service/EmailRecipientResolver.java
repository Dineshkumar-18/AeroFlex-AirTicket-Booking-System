package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.EmailRecipient;
import com.aeroflex.notificationservice.enums.NotificationChannel;
import com.aeroflex.notificationservice.model.NotificationUserProfile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailRecipientResolver
        implements ChannelRecipientResolver<EmailRecipient> {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public EmailRecipient resolve(
            NotificationUserProfile profile) {

        if (!profile.isEmailVerified()) {
            throw new IllegalStateException(
                    "Email is not verified for user: "
                            + profile.getUserId()
            );
        }

        return new EmailRecipient(
                List.of(profile.getEmail()),
                List.of(),
                List.of()
        );
    }
}
