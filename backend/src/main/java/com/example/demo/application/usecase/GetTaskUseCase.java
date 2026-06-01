package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(GetTaskUseCase.class);
    private final TaskRepository taskRepository;

    public GetTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDto> findAll() {
        List<TaskDto> tasks = taskRepository.findAll().stream()
                .map(TaskDto::fromDomain)
                .toList();
        log.debug("Found {} tasks", tasks.size());
        return tasks;
    }

    public Optional<TaskDto> findById(Long id) {
        log.debug("Finding task by id: {}", id);
        return taskRepository.findById(new TaskId(id))
                .map(TaskDto::fromDomain);
    }

    public List<TaskDto> findByCompleted(boolean completed) {
        List<TaskDto> tasks = taskRepository.findByCompleted(completed).stream()
                .map(TaskDto::fromDomain)
                .toList();
        log.debug("Found {} tasks with completed={}", tasks.size(), completed);
        return tasks;
    }

    public List<TaskDto> search(String keyword) {
        List<TaskDto> tasks = taskRepository.findByTitleContaining(keyword).stream()
                .map(TaskDto::fromDomain)
                .toList();
        log.debug("Found {} tasks matching keyword: {}", tasks.size(), keyword);
        return tasks;
    }
}
