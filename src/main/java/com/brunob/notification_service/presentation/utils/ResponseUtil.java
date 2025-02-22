package com.brunob.notification_service.presentation.utils;

import com.brunob.notification_service.presentation.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseUtil {
    private static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, String message, String details, T data) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .message(message)
                .details(details)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return buildResponse(HttpStatus.OK, message, null, data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message, String details) {
        return buildResponse(HttpStatus.BAD_REQUEST, message, details, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> notFound(String message, String details) {
        return buildResponse(HttpStatus.NOT_FOUND, message, details, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message, String details) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, details, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> forbidden(String message, String details) {
        return buildResponse(HttpStatus.FORBIDDEN, message, details, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> unauthorized(String message, String details) {
        return buildResponse(HttpStatus.UNAUTHORIZED, message, details, null);
    }
}
