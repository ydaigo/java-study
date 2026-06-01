package com.example.demo.presentation.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * エラーレスポンスのDTO
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> details
) {
    public ErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> details) {
        this(LocalDateTime.now(), status, error, message, path, details);
    }
}
