package com.aeroflex.common.notification.options;

import com.aeroflex.common.notification.attachment.Attachment;
import com.aeroflex.common.notification.enums.NotificationChannelType;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest implements ChannelRequest {

    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    private String from;

    private String replyTo;

    private List<Attachment> attachments;

    private boolean purgeAfterSend;

    @Override
    public NotificationChannelType getChannel() {
        return NotificationChannelType.EMAIL;
    }

}
