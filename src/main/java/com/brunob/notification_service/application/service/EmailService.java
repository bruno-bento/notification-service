package com.brunob.notification_service.application.service;

import com.brunob.notification_service.domain.model.smtp.SmtpConfig;
import com.brunob.notification_service.presentation.dto.BounceNotificationDTO;
import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.domain.repository.EmailRepository;
import com.brunob.notification_service.presentation.dto.EmailRequestDTO;
import com.brunob.notification_service.infrastructure.mail.EmailSender;
import com.brunob.notification_service.infrastructure.messaging.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService implements NotificationService<Email> {
    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private NotificationProducer<Email> producer;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private  SmtpConfigService smtpConfigService;

    @Override
    public Email create(Object request) {
        EmailRequestDTO dto = (EmailRequestDTO) request;
        Email email = Email.builder()
                .recipient(dto.getRecipient())
                .subject(dto.getSubject())
                .body(dto.getBody())
                .smtpId(dto.getSmtpId())
                .build();
        return emailRepository.save(email);
    }

    @Override
    public void send(Long id) {
        producer.sendToQueue(id);
    }

    @Override
    public List<Email> listAll() {
        return emailRepository.findAll();
    }

    public void markEmailAsOpened(String trackingId) {
         emailRepository.findByTrackingId(trackingId).ifPresent(email -> {
             email.markAsOpened();
             emailRepository.save(email);
        });
    }

    public void processEmail(Long emailId) {
        emailRepository.findById(emailId).ifPresent(email -> {
            if (!email.canRetry()) return;

            try {
                SmtpConfig smtpConfig = (email.getSmtpId() != null) ?
                        smtpConfigService.getById(email.getSmtpId())
                        : smtpConfigService.chooseLeastUsedSmtp();

                emailSender.send(email, smtpConfig);
                email.markAsSent();
                System.out.println("Email sent successfully: " + emailId);
            } catch (Exception e) {
                email.markAsFailed();
                producer.sendToQueue(email.getId());
            }

            emailRepository.save(email);
        });
    }

    public void handleBounce(BounceNotificationDTO bounce) {
        emailRepository.findByTrackingId(bounce.getTrackingId()).ifPresent(email -> {
            email.markAsBounced(bounce.getReason());
            emailRepository.save(email);
        });
    }
}