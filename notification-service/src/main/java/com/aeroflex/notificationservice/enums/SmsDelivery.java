package com.aeroflex.notificationservice.enums;

public record SmsDelivery(
        NotificationDeliveryContext context,
        SmsRecipient recipient,
        String content
) implements ChannelDelivery {
}
