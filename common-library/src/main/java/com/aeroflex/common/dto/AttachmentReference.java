package com.aeroflex.common.dto;

public record AttachmentReference(
        String fileName,
        String storageKey,
        String contentType,
        long size
) {
}
