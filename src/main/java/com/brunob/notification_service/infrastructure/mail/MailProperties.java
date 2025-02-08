package com.brunob.notification_service.infrastructure.mail;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class MailProperties {
    private String from;
}
