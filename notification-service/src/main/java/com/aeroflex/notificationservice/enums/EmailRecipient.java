package com.aeroflex.notificationservice.enums;

import com.aeroflex.notificationservice.service.ChannelRecipient;

import java.util.List;

public record EmailRecipient(
        List<String> to,
        List<String> cc,
        List<String> bcc
) implements ChannelRecipient {
}
