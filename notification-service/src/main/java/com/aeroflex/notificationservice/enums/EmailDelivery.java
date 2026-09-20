package com.aeroflex.notificationservice.enums;

import com.aeroflex.common.dto.AttachmentReference;
import com.aeroflex.common.notification.attachment.Attachment;

import java.util.List;

public record EmailDelivery(
        NotificationDeliveryContext context,
        EmailRecipient recipient,
        String subject,
        String content,
        List<Attachment> attachments
) implements ChannelDelivery {
}
