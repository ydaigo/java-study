package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タスク作成ユースケース
 */
@Service
@Transactional
public class CreateTaskUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateTaskUseCase.class);
    private final TaskRepository taskRepository;

    public CreateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDto execute(String title, String description) {
        Task task = Task.create(title, description);
        Task savedTask = taskRepository.save(task);
        log.info("Task created: id={}, title={}", savedTask.getId().getValue(), title);
        return TaskDto.fromDomain(savedTask);
    }
}
