package com.example.demo.application.usecase;

import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タスク削除ユースケース
 */
@Service
@Transactional
public class DeleteTaskUseCase {

    private final TaskRepository taskRepository;

    public DeleteTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public boolean execute(Long id) {
        TaskId taskId = new TaskId(id);
        return taskRepository.findById(taskId)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                })
                .orElse(false);
    }
}
