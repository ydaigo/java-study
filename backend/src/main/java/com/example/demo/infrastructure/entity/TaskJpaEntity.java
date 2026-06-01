package com.example.demo.infrastructure.entity;

import com.example.demo.domain.model.Task;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * タスクJPAエンティティ（Infrastructure層）
 */
@Entity
@Table(name = "tasks")
public class TaskJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private boolean completed;

    private LocalDateTime createdAt;

    public TaskJpaEntity() {
    }

    public TaskJpaEntity(Long id, String title, String description, boolean completed, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    /**
     * ドメインモデルからJPAエンティティへ変換
     */
    public static TaskJpaEntity fromDomain(Task task) {
        return new TaskJpaEntity(
                task.getId().getValue(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt()
        );
    }

    /**
     * JPAエンティティからドメインモデルへ変換
     */
    public Task toDomain() {
        return Task.reconstruct(id, title, description, completed, createdAt);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
