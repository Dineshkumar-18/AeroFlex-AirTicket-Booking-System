package com.aeroflex.notificationservice.service;

import com.aeroflex.common.dto.AttachmentReference;
import com.aeroflex.common.notification.attachment.Attachment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachmentResolver {

    private final AttachmentStorage storage;

    public List<Attachment> resolve(
            List<AttachmentReference> references) {

        if (references == null || references.isEmpty()) {
            return List.of();
        }

        return references.stream()
                .map(this::resolve)
                .toList();
    }

    private Attachment resolve(
            AttachmentReference reference) {

        byte[] content =
                storage.download(
                        reference.storageKey()
                );

        return new Attachment(
                reference.fileName(),
                reference.contentType(),
                content
        );
    }
}
