package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.application.dto.BounceNotificationDTO;
import com.brunob.notification_service.application.dto.EmailRequestDTO;
import com.brunob.notification_service.application.service.EmailService;
import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.infrastructure.mail.TrackingPixelUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public String sendEmail(@RequestBody EmailRequestDTO request) {
        Email email = emailService.create(request);
        emailService.send(email.getId());
        return "Email queued for sending! ID: " + email.getId();
    }

    @GetMapping
    @Operation(summary = "List emails", description = "This endpoint list all e-mails")
    public List<Email> listEmails() {
        return emailService.listAll();
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
    public ResponseEntity<Void> handleBounce(@RequestBody BounceNotificationDTO bounce) {
        emailService.handleBounce(bounce);
        return ResponseEntity.ok().build();
    }
}
