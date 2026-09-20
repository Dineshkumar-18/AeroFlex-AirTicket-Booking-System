package com.aeroflex.common.events;

import com.aeroflex.common.dto.AttachmentReference;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
@Builder
public record BusinessEvent<T>(
        String eventId,
        String eventType,
        String eventVersion,
        Instant occurredAt,
        String source,
        String userId,
        String correlationId,
        T payload,
        List<AttachmentReference> attachments
) {
}
