package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
public class SmsTemplateProcessor
        implements TemplateProcessor {

    private final SpringTemplateEngine templateEngine;

    public SmsTemplateProcessor(
            @Qualifier("smsTemplateEngine")
            SpringTemplateEngine templateEngine) {

        this.templateEngine = templateEngine;
    }

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.SMS;
    }

    @Override
    public String process(
            String templateCode,
            String templateVersion,
            Map<String, Object> variables) {

        Context context = new Context();
        context.setVariables(variables);

        return templateEngine.process(
                templateCode,
                context
        );
    }
}
