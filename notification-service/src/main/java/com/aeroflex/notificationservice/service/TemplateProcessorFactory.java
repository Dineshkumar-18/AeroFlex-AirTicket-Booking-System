package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.NotificationChannel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class TemplateProcessorFactory {

    private final Map<NotificationChannel, TemplateProcessor> processors;

    public TemplateProcessorFactory(
            List<TemplateProcessor> processorList) {

        this.processors = processorList.stream()
                .collect(Collectors.toMap(
                        TemplateProcessor::channel,
                        Function.identity()
                ));
    }

    public TemplateProcessor get(
            NotificationChannel channel) {

        TemplateProcessor processor =
                processors.get(channel);

        if (processor == null) {
            throw new IllegalArgumentException(
                    "No template processor for channel: "
                            + channel
            );
        }

        return processor;
    }
}
