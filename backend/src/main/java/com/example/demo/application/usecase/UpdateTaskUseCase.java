package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タスク更新ユースケース
 */
@Service
@Transactional
public class UpdateTaskUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateTaskUseCase.class);
    private final TaskRepository taskRepository;

    public UpdateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDto execute(Long id, String title, String description, Boolean completed) {
        TaskId taskId = new TaskId(id);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        if (title != null) {
            task.changeTitle(title);
        }
        if (description != null) {
            task.changeDescription(description);
        }
        if (completed != null) {
            if (completed) {
                task.complete();
            } else {
                task.uncomplete();
            }
        }

        Task savedTask = taskRepository.save(task);
        log.info("Task updated: id={}", id);
        return TaskDto.fromDomain(savedTask);
    }
}
