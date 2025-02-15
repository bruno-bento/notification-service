package com.brunob.notification_service.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSetupRequest {
    private String username;
    private String password;
}