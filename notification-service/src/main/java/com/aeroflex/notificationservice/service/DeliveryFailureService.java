package com.aeroflex.notificationservice.service;

import com.aeroflex.notificationservice.enums.DeliveryStatus;
import com.aeroflex.notificationservice.model.NotificationDelivery;
import com.aeroflex.notificationservice.repository.NotificationDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryFailureService {

    private final NotificationDeliveryRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(
            UUID deliveryId,
            String errorMessage) {

        NotificationDelivery delivery =
                repository.findById(deliveryId)
                        .orElseThrow();

        delivery.setStatus(
                DeliveryStatus.FAILED
        );

        delivery.setErrorMessage(
                errorMessage
        );

        repository.save(delivery);
    }
}
