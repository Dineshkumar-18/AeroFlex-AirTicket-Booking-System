package com.aeroflex.common.notification.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private boolean success;

    private String message;

    private String notificationId;

}