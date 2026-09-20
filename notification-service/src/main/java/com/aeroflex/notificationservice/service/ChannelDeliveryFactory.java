package com.aeroflex.notificationservice.service;

import com.aeroflex.common.dto.AttachmentReference;
import com.aeroflex.common.notification.attachment.Attachment;
import com.aeroflex.notificationservice.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChannelDeliveryFactory {

    private final AttachmentResolver attachmentResolver;

    public ChannelDelivery create(
            NotificationDeliveryContext context,
            NotificationChannel channel,
            ChannelRecipient recipient,
            String subject,
            String content,
            List<AttachmentReference> attachmentReferences) {

        return switch (channel) {

            case EMAIL -> {

                EmailRecipient emailRecipient =
                        (EmailRecipient) recipient;

                List<Attachment> attachments =
                        attachmentResolver.resolve(
                                attachmentReferences
                        );

                yield new EmailDelivery(
                        context,
                        emailRecipient,
                        subject,
                        content,
                        attachments
                );
            }

            case SMS -> {

                SmsRecipient smsRecipient =
                        (SmsRecipient) recipient;

                yield new SmsDelivery(
                        context,
                        smsRecipient,
                        content
                );
            }

        };
    }
}
