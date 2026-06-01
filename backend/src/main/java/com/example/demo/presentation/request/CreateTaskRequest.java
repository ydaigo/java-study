package com.example.demo.presentation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * タスク作成リクエスト
 */
public record CreateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be 255 characters or less")
        String title,

        @Size(max = 1000, message = "Description must be 1000 characters or less")
        String description
) {
}
