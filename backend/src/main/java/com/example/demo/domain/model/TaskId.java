package com.example.demo.domain.model;

import java.util.Objects;

/**
 * タスクIDの値オブジェクト
 */
public class TaskId {

    private final Long value;

    public TaskId(Long value) {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("TaskId must be positive");
        }
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskId taskId = (TaskId) o;
        return Objects.equals(value, taskId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
