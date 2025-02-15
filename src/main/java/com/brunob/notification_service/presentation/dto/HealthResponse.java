package com.brunob.notification_service.presentation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HealthResponse {
    private String uptime;
}
