package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.application.service.SmtpConfigService;
import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.domain.model.smtp.SmtpConfig;
import com.brunob.notification_service.presentation.dto.ApiResponse;
import com.brunob.notification_service.presentation.dto.SmtpConfigRequestDTO;
import com.brunob.notification_service.presentation.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/smtp")
public class SmtpConfigController {

    @Autowired
    private SmtpConfigService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SmtpConfig>>> listSmtps() {
        return ResponseUtil.success(service.listAll(), "Smtps retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SmtpConfig>> save(@RequestBody SmtpConfigRequestDTO config) {
        return ResponseUtil.success(service.create(config), "Smtp create successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseUtil.success(null, "Smtp deleted successfully");
    }
}