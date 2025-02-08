package com.brunob.notification_service.infrastructure.messaging.rabbitmq.config;


import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Autowired
    private RabbitMQProperties properties;

    @Bean
    public Queue emailQueue() {
        return new Queue(properties.getQueue().getEmail(), true);
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(properties.getExchange().getEmail());
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(properties.getRoutingKey().getEmail());
    }

    public String getExchangeName() {
        return properties.getExchange().getEmail();
    }

    public String getRoutingKey() {
        return properties.getRoutingKey().getEmail();
    }

    public String getQueueName() {
        return properties.getQueue().getEmail();
    }

    @PostConstruct
    public void init() {
        System.out.println("RabbitMQ Configuration initialized");
        System.out.println("Queue name: " + this.getQueueName());
        System.out.println("Exchange name: " + this.getExchangeName());
        System.out.println("Routing key: " + this.getRoutingKey());
    }
}