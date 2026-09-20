package com.aeroflex.common.notification.dto;

import com.aeroflex.common.notification.enums.NotificationTemplate;
import com.aeroflex.common.notification.options.ChannelRequest;
import com.aeroflex.common.notification.template.TemplateModel;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {

    private NotificationTemplate template;

    private TemplateModel model;

    private List<ChannelRequest> channels;

}