package com.brunob.notification_service.presentation.exception;

import com.brunob.notification_service.presentation.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Erro ao processar o JSON fornecido. Verifique a sintaxe e formatação.";

        ex.printStackTrace();

        ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .details(ex.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .data(null) // Dados não são necessários para o erro
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Tipo de argumento inválido.";

        ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .details(ex.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGlobalException(Exception ex) {
        String errorMessage = "Ocorreu um erro inesperado.";

        ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(errorMessage)
                .details(ex.getLocalizedMessage())
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("Access Denied")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Invalid credentials")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException ex) {
        ApiResponse<String> errorResponse = ApiResponse.<String>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Failed to create admin")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


}
