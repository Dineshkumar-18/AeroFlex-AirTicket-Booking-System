package com.aeroflex.notificationservice.builder;

import com.aeroflex.common.events.BusinessEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TemplateDataBuilder {

    private final ObjectMapper objectMapper;

    public Map<String, Object> build(BusinessEvent<?> event) {

        return objectMapper.convertValue(
                event.payload(),
                new TypeReference<Map<String, Object>>() {}
        );
    }
}