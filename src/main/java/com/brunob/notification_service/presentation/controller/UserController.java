package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.application.service.UserService;
import com.brunob.notification_service.infrastructure.security.annotation.PublicRoute;
import com.brunob.notification_service.presentation.dto.UserSetupRequest;
import com.brunob.notification_service.presentation.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private @Autowired UserService userService;

    @PublicRoute
    @GetMapping("/check-admin")
    public ResponseEntity<ApiResponse<Boolean>> checkAdmin() {
        boolean exists = userService.adminExists();
        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .status(200)
                .message("Admin user exists check")
                .details(null)
                .timestamp(LocalDateTime.now())
                .data(exists)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/setup-admin")
    @PublicRoute
    public ResponseEntity<ApiResponse<String>> setupAdmin(@RequestBody UserSetupRequest request) {
        userService.setupAdmin(request.getUsername(), request.getPassword());
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(200)
                .message("Admin user created successfully")
                .details(null)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
