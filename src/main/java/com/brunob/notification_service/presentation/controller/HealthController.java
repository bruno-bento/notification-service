package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.infrastructure.security.annotation.PublicRoute;
import com.brunob.notification_service.presentation.dto.ApiResponse;
import com.brunob.notification_service.presentation.dto.HealthResponse;
import com.brunob.notification_service.presentation.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HealthController {

    private static final LocalDateTime START_TIME = LocalDateTime.now();

    @GetMapping("/health")
    @PublicRoute
    public ResponseEntity<ApiResponse<HealthResponse>> healthCheck() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(START_TIME, now);
        Period period = Period.between(START_TIME.toLocalDate(), now.toLocalDate());

        String uptime = formatUptime(period, duration);

        HealthResponse healthResponse = HealthResponse.builder().uptime(uptime).build();

        return ResponseUtil.success(healthResponse, "Service is running");

    }

    private String formatUptime(Period period, Duration duration) {
        List<String> parts = new ArrayList<>();

        addUnit(parts, period.getMonths(), "month");
        addUnit(parts, period.getDays(), "day");
        addUnit(parts, duration.toHours(), "hour");
        addUnit(parts, duration.toMinutes() % 60, "minute");

        if (parts.isEmpty()) {
            return "0 minutes";
        }

        return String.join(", ", parts);
    }

    private void addUnit(List<String> parts, long value, String unit) {
        if (value > 0) {
            parts.add(value + " " + unit + (value > 1 ? "s" : ""));
        }
    }
}
