package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タスク作成ユースケース
 */
@Service
@Transactional
public class CreateTaskUseCase {

    private final TaskRepository taskRepository;

    public CreateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDto execute(String title, String description) {
        Task task = Task.create(title, description);
        Task savedTask = taskRepository.save(task);
        return TaskDto.fromDomain(savedTask);
    }
}
