package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.application.service.SmtpConfigService;
import com.brunob.notification_service.domain.model.smtp.SmtpConfig;
import com.brunob.notification_service.presentation.dto.ApiResponse;
import com.brunob.notification_service.presentation.dto.BounceNotificationDTO;
import com.brunob.notification_service.presentation.dto.EmailRequestDTO;
import com.brunob.notification_service.application.service.EmailService;
import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.infrastructure.mail.TrackingPixelUtil;
import com.brunob.notification_service.presentation.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/emails")
@Tag(name = "E-mails", description = "API for send de e-mails")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private SmtpConfigService smtpConfigService;

    @Autowired
    private TrackingPixelUtil trackingPixelUtil;

    @PostMapping("/send")
    @Operation(summary = "Send email to queue", description = "This endpoint creates an e-mail and sends it to the processing queue")
    public ResponseEntity<ApiResponse<String>> sendEmail(@RequestBody EmailRequestDTO request) {
        try {
            if (!smtpConfigService.isSmtpValid(request.getSmtpId())) {
                return ResponseUtil.notFound("Servidor SMTP não encontrado", "ID: " + request.getSmtpId());
            }

            Email email = emailService.create(request);
            emailService.send(email.getId());

            return ResponseUtil.success("ID: " + email.getId(), "Email queued for sending!");
        } catch (IllegalStateException e) {
            return ResponseUtil.badRequest("Erro no envio", e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseUtil.notFound("Erro no envio", e.getMessage());
        }
    }


    @GetMapping
    @Operation(summary = "List emails", description = "This endpoint list all e-mails")
    public ResponseEntity<ApiResponse<List<Email>>> listEmails() {
        List<Email> emails = emailService.listAll();

        return ResponseUtil.success(emails, "Emails retrieved successfully");
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

        return ResponseUtil.success(null, "Bounce notification handled successfully");
    }

}
