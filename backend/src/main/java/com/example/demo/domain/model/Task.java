package com.example.demo.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * タスクエンティティ（ドメインモデル）
 */
public class Task {

    private TaskId id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;

    private Task(TaskId id, String title, String description, boolean completed, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    /**
     * 新規タスク作成用ファクトリメソッド
     */
    public static Task create(String title, String description) {
        validateTitle(title);
        return new Task(
                new TaskId(null),
                title,
                description,
                false,
                LocalDateTime.now()
        );
    }

    /**
     * 永続化データからの復元用ファクトリメソッド
     */
    public static Task reconstruct(Long id, String title, String description, boolean completed, LocalDateTime createdAt) {
        return new Task(
                new TaskId(id),
                title,
                description,
                completed,
                createdAt
        );
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (title.length() > 255) {
            throw new IllegalArgumentException("Title must be 255 characters or less");
        }
    }

    public void changeTitle(String newTitle) {
        validateTitle(newTitle);
        this.title = newTitle;
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void complete() {
        this.completed = true;
    }

    public void uncomplete() {
        this.completed = false;
    }

    // Getters
    public TaskId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // IDをセットするためのメソッド（リポジトリ実装用）
    public void assignId(Long id) {
        this.id = new TaskId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
