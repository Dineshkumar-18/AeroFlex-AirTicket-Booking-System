package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.dto.NotificationDeliveryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService {

    private final TemplateProcessorFactory processorFactory;

    public String render(NotificationDeliveryEvent event) {

        TemplateProcessor processor =
                processorFactory.get(event.channel());

        return processor.process(
                event.templateCode(),
                event.templateVersion(),
                event.templateData()
        );
    }
}
