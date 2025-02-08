package com.brunob.notification_service.infrastructure.messaging.rabbitmq;

import com.brunob.notification_service.infrastructure.messaging.NotificationProducer;
import com.brunob.notification_service.infrastructure.messaging.rabbitmq.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer implements NotificationProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig config;

    @Override
    public void sendToQueue(Long notificationId) {
        System.out.println("Email send to queue ID: " + notificationId);
        rabbitTemplate.convertAndSend(
                config.getExchangeName(),
                config.getRoutingKey(),
                notificationId
        );
    }
}