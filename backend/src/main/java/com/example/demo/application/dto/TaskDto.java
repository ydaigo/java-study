package com.example.demo.application.dto;

import com.example.demo.domain.model.Task;
import java.time.LocalDateTime;

/**
 * タスクDTO（Application層）
 */
public record TaskDto(
        Long id,
        String title,
        String description,
        boolean completed,
        LocalDateTime createdAt
) {
    public static TaskDto fromDomain(Task task) {
        return new TaskDto(
                task.getId().getValue(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt()
        );
    }
}
