package com.brunob.notification_service.presentation.dto;

import lombok.Data;

@Data
public class BounceNotificationDTO {
    private String trackingId;
    private String reason;
}