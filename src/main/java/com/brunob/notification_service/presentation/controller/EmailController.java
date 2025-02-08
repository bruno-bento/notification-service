package com.brunob.notification_service.presentation.controller;

import com.brunob.notification_service.application.dto.EmailRequestDTO;
import com.brunob.notification_service.application.service.EmailService;
import com.brunob.notification_service.domain.model.email.Email;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {
    @Autowired
    private EmailService emailService;

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
}
