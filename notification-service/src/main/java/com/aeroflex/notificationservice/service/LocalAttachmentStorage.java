package com.aeroflex.notificationservice.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class LocalAttachmentStorage
        implements AttachmentStorage {

    @Override
    public byte[] download(String storageKey) {

        try {
            Path path = Paths.get(storageKey);

            return Files.readAllBytes(path);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Failed to read attachment: "
                            + storageKey,
                    e
            );
        }
    }
}
