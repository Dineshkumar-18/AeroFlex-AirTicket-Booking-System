package com.aeroflex.common.notification.attachment;

import lombok.*;

public record Attachment(
        String fileName,
        String contentType,
        byte[] content
) {
}
