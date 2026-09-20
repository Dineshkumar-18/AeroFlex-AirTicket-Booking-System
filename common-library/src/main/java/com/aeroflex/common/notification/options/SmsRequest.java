package com.aeroflex.common.notification.options;

import com.aeroflex.common.notification.enums.NotificationChannelType;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest implements ChannelRequest {

    private List<String> phoneNumbers;

    private String senderId;

    @Override
    public NotificationChannelType getChannel() {
        return NotificationChannelType.SMS;
    }

}
