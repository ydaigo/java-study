package com.example.demo.domain.repository;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import java.util.List;
import java.util.Optional;

/**
 * タスクリポジトリインターフェース（ドメイン層）
 */
public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(TaskId id);

    List<Task> findAll();

    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContaining(String keyword);

    void delete(Task task);

    boolean existsById(TaskId id);
}
