package com.aeroflex.identityservice.user.service;

import java.util.UUID;

public interface FlightOwnerApprovalService {

    void approve(
            UUID userId,
            String remarks
    );

    void reject(
            UUID userId,
            String remarks
    );
}
