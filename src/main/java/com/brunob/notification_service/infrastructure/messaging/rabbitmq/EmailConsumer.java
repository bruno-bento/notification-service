package com.brunob.notification_service.infrastructure.messaging.rabbitmq;


import com.brunob.notification_service.domain.model.email.Email;
import com.brunob.notification_service.domain.repository.NotificationRepository;
import com.brunob.notification_service.infrastructure.mail.EmailSender;
import com.brunob.notification_service.infrastructure.messaging.NotificationProducer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    @Autowired
    private NotificationRepository<Email> repository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private NotificationProducer<Email> producer;

    @RabbitListener(queues = "#{rabbitMQConfig.queueName}")
    public void processEmail(Long emailId) {
        repository.findById(emailId).ifPresent(email -> {
            if (!email.canRetry()) return;

            try {
                emailSender.send(email);
                email.markAsSent();
                System.out.println("Email sent successfully: " + emailId );
            } catch (Exception e) {
                email.markAsFailed();
                producer.sendToQueue(email.getId());
            }

            repository.save(email);
        });
    }
}