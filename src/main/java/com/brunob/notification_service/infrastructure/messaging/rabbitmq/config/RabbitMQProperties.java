package com.brunob.notification_service.infrastructure.messaging.rabbitmq.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
@Getter
@Setter
public class RabbitMQProperties {
    private Queue queue = new Queue();
    private Exchange exchange = new Exchange();
    private RoutingKey routingKey = new RoutingKey();

    @Getter
    @Setter
    public static class Queue {
        private String email;
    }

    @Getter
    @Setter
    public static class Exchange {
        private String email;
    }

    @Getter
    @Setter
    public static class RoutingKey {
        private String email;
    }
}
