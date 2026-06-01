package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * タスク取得ユースケース
 */
@Service
@Transactional(readOnly = true)
public class GetTaskUseCase {

    private final TaskRepository taskRepository;

    public GetTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskDto::fromDomain)
                .toList();
    }

    public Optional<TaskDto> findById(Long id) {
        return taskRepository.findById(new TaskId(id))
                .map(TaskDto::fromDomain);
    }

    public List<TaskDto> findByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed).stream()
                .map(TaskDto::fromDomain)
                .toList();
    }

    public List<TaskDto> search(String keyword) {
        return taskRepository.findByTitleContaining(keyword).stream()
                .map(TaskDto::fromDomain)
                .toList();
    }
}
