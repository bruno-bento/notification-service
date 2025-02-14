package com.brunob.notification_service.infrastructure.messaging.rabbitmq;


import com.brunob.notification_service.application.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "#{rabbitMQConfig.queueName}")
    public void processEmail(Long emailId) {
        emailService.processEmail(emailId);
    }
}