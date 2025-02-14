package com.brunob.notification_service.application.dto;

import lombok.Data;

@Data
public class BounceNotificationDTO {
    private String trackingId;
    private String reason;
}