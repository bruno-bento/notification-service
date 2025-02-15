package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.presentation.dto.ApiResponse;
import com.brunob.notification_service.presentation.dto.BounceNotificationDTO;
import com.brunob.notification_service.presentation.dto.EmailRequestDTO;
import com.brunob.notification_service.application.service.EmailService;
import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.infrastructure.mail.TrackingPixelUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private TrackingPixelUtil trackingPixelUtil;

    @PostMapping("/send")
    @Operation(summary = "Send email to queue", description = "This endpoint creates an e-mail and sends it to the processing queue")
    public ResponseEntity<ApiResponse<String>>  sendEmail(@RequestBody EmailRequestDTO request) {
        Email email = emailService.create(request);
        emailService.send(email.getId());
        ApiResponse<String> response = ApiResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .status(200)
                .message("Email queued for sending!")
                .data("ID: " + email.getId())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List emails", description = "This endpoint list all e-mails")
    public ResponseEntity<ApiResponse<List<Email>>> listEmails() {
        List<Email> emails = emailService.listAll();
        ApiResponse<List<Email>> response = ApiResponse.<List<Email>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("Emails retrieved successfully")
                .data(emails)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/track/{trackingId}")
    @Operation(summary = "Track email open", description = "This endpoint tracks when an email is opened")
    public ResponseEntity<byte[]> trackEmailOpen(@PathVariable String trackingId) {

        CompletableFuture.runAsync(() -> {
            emailService.markEmailAsOpened(trackingId);
        });

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(trackingPixelUtil.getTrackingPixel());
    }

    @PostMapping("/bounce")
    @Operation(summary = "Handle bounce notification", description = "This endpoint handles email bounce notifications")
    public ResponseEntity<ApiResponse<Void>> handleBounce(@RequestBody BounceNotificationDTO bounce) {
        emailService.handleBounce(bounce);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("Bounce notification handled successfully")
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

}
