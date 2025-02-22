package com.brunob.notification_service.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "SMTP configuration request data")
public class SmtpConfigRequestDTO {

    @Schema(description = "SMTP server host", example = "smtp.example.com")
    private String host;

    @Schema(description = "SMTP server port", example = "587")
    private Integer port;

    @Schema(description = "SMTP username for authentication", example = "user@example.com")
    private String username;

    @Schema(description = "SMTP password for authentication", example = "strongpassword123")
    private String password;

    @Schema(description = "Indicates if TLS should be used", example = "true")
    private Boolean useTls;

    @Schema(description = "Indicates if SSL should be used", example = "false")
    private Boolean useSsl;

    @Schema(description = "Daily email sending limit", example = "500")
    private Integer dailyLimit;

    @Schema(description = "Monthly email sending limit", example = "15000")
    private Integer monthlyLimit;
}
