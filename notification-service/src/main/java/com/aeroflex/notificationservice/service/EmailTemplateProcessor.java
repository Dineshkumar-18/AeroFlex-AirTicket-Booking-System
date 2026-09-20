package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Component
public class EmailTemplateProcessor
        implements TemplateProcessor {

    private final SpringTemplateEngine templateEngine;

    public EmailTemplateProcessor(
            @Qualifier("emailTemplateEngine")
            SpringTemplateEngine templateEngine) {

        this.templateEngine = templateEngine;
    }

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.EMAIL;
    }

    @Override
    public String process(
            String templateCode,
            String templateVersion,
            Map<String, Object> variables) {

        Context context = new Context();
        context.setVariables(variables);

        String templateName =
                templateCode + "_" + templateVersion;

        return templateEngine.process(
                templateName,
                context
        );
    }
}
