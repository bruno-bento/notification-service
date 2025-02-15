package com.brunob.notification_service.presentation.dto;
import lombok.Getter;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@AllArgsConstructor
@Schema(description = "E-mail request data")
public class EmailRequestDTO {
    @Schema(description = "E-mail recipient ", example = "user@example.com")
    private String recipient;

    @Schema(description = "E-mail subject", example = "Recovery password")
    private String subject;

    @Schema(description = "E-mail body", example = "to recovery email, click here")
    private String body;
}

