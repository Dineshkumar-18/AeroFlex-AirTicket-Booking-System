package com.aeroflex.common.notification.options;

import com.aeroflex.common.notification.enums.NotificationChannelType;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PushRequest implements ChannelRequest {

    private List<String> deviceTokens;

    @Override
    public NotificationChannelType getChannel() {
        return NotificationChannelType.PUSH;
    }

}
