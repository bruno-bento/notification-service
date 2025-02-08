package com.brunob.notification_service.application.service;

import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.domain.repository.NotificationRepository;
import com.brunob.notification_service.application.dto.EmailRequestDTO;
import com.brunob.notification_service.infrastructure.messaging.NotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService implements NotificationService<Email> {
    @Autowired
    private NotificationRepository<Email> emailRepository;

    @Autowired
    private NotificationProducer<Email> producer;

    @Override
    public Email create(Object request) {
        EmailRequestDTO dto = (EmailRequestDTO) request;
        Email email = Email.builder()
                .recipient(dto.getRecipient())
                .subject(dto.getSubject())
                .body(dto.getBody())
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
}