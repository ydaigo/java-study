package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * タスク更新ユースケース
 */
@Service
@Transactional
public class UpdateTaskUseCase {

    private final TaskRepository taskRepository;

    public UpdateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<TaskDto> execute(Long id, String title, String description, Boolean completed) {
        return taskRepository.findById(new TaskId(id))
                .map(task -> {
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
                    return TaskDto.fromDomain(savedTask);
                });
    }
}
