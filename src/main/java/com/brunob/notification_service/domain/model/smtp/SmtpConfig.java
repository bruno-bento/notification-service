package com.brunob.notification_service.domain.model.smtp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "smtp_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmtpConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String host;
    private Integer port;
    private String username;
    private String password;
    private Boolean useTls;
    private Boolean useSsl;

    private Integer dailyLimit;
    private Integer monthlyLimit;
}
