package com.example.demo.application.usecase;

import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * タスク削除ユースケース
 */
@Service
@Transactional
public class DeleteTaskUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteTaskUseCase.class);
    private final TaskRepository taskRepository;

    public DeleteTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void execute(Long id) {
        TaskId taskId = new TaskId(id);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
        taskRepository.delete(task);
        log.info("Task deleted: id={}", id);
    }
}
