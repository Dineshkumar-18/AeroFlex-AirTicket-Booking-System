package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.model.ProcessedEvent;
import com.aeroflex.notificationservice.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class IdempotencyService {

    private final ProcessedEventRepository repository;

    public boolean isProcessed(String eventId) {
        return repository.existsByEventId(eventId);
    }

    public void markProcessed(String eventId) {

        ProcessedEvent event = new ProcessedEvent();

        event.setEventId(eventId);
        event.setProcessedAt(Instant.now());

        repository.save(event);
    }
}
