package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.application.service.UserService;
import com.brunob.notification_service.infrastructure.security.annotation.PublicRoute;
import com.brunob.notification_service.presentation.dto.UserSetupRequest;
import com.brunob.notification_service.presentation.dto.ApiResponse;
import com.brunob.notification_service.presentation.utils.ResponseUtil;
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
        return ResponseUtil.success(exists, "Admin user exists check");
    }

    @PostMapping("/setup-admin")
    @PublicRoute
    public ResponseEntity<ApiResponse<String>> setupAdmin(@RequestBody UserSetupRequest request) {
        userService.setupAdmin(request.getUsername(), request.getPassword());

        return ResponseUtil.success(null, "Admin user created successfully");
    }
}
