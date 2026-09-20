package com.aeroflex.notificationservice.enums;

import com.aeroflex.notificationservice.service.ChannelRecipient;

public record SmsRecipient(
        String mobileNumber
) implements ChannelRecipient {
}
