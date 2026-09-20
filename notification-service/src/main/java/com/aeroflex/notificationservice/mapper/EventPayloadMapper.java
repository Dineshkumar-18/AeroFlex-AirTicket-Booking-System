package com.aeroflex.notificationservice.mapper;

import com.aeroflex.common.events.BusinessEvent;
import com.aeroflex.common.events.booking.BookingConfirmedPayload;
import com.aeroflex.common.events.user.UserProfileData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventPayloadMapper {

    private final ObjectMapper objectMapper;

    private final Map<String, Class<?>> eventPayloadTypes =
            Map.of(
                    "USER_PROFILE_UPDATED",
                    UserProfileData.class,

                    "BOOKING_CONFIRMED",
                    BookingConfirmedPayload.class
            );

    public Object map(
            BusinessEvent<Object> event) {

        Class<?> targetType =
                eventPayloadTypes.get(event.eventType());

        if (targetType == null) {
            throw new IllegalArgumentException(
                    "Unsupported event type: "
                            + event.eventType()
            );
        }

        return objectMapper.convertValue(
                event.payload(),
                targetType
        );
    }
}
