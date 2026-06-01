package com.example.demo.presentation.response;

import com.example.demo.application.dto.TaskDto;
import java.time.LocalDateTime;

/**
 * タスクレスポンス
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt
) {
    public static TaskResponse fromDto(TaskDto dto) {
        return new TaskResponse(
                dto.id(),
                dto.title(),
                dto.description(),
                dto.completed(),
                dto.createdAt()
        );
    }
}
