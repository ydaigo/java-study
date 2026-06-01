package com.example.demo.presentation.controller;

import com.example.demo.application.usecase.CreateTaskUseCase;
import com.example.demo.application.usecase.DeleteTaskUseCase;
import com.example.demo.application.usecase.GetTaskUseCase;
import com.example.demo.application.usecase.UpdateTaskUseCase;
import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.presentation.request.CreateTaskRequest;
import com.example.demo.presentation.request.UpdateTaskRequest;
import com.example.demo.presentation.response.TaskResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * タスクコントローラー（Presentation層）
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger log = LoggerFactory.getLogger(TaskController.class);

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public TaskController(
            CreateTaskUseCase createTaskUseCase,
            GetTaskUseCase getTaskUseCase,
            UpdateTaskUseCase updateTaskUseCase,
            DeleteTaskUseCase deleteTaskUseCase
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        log.debug("GET /api/tasks - Fetching all tasks");
        return getTaskUseCase.findAll().stream()
                .map(TaskResponse::fromDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        log.debug("GET /api/tasks/{} - Fetching task", id);
        return getTaskUseCase.findById(id)
                .map(TaskResponse::fromDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }

    @GetMapping("/status")
    public List<TaskResponse> getTasksByStatus(@RequestParam boolean completed) {
        log.debug("GET /api/tasks/status?completed={} - Fetching tasks by status", completed);
        return getTaskUseCase.findByCompleted(completed).stream()
                .map(TaskResponse::fromDto)
                .toList();
    }

    @GetMapping("/search")
    public List<TaskResponse> searchTasks(@RequestParam String keyword) {
        log.debug("GET /api/tasks/search?keyword={} - Searching tasks", keyword);
        return getTaskUseCase.search(keyword).stream()
                .map(TaskResponse::fromDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        log.info("POST /api/tasks - Creating task: title={}", request.title());
        var dto = createTaskUseCase.execute(request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.fromDto(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        log.info("PUT /api/tasks/{} - Updating task", id);
        var dto = updateTaskUseCase.execute(id, request.title(), request.description(), request.completed());
        return ResponseEntity.ok(TaskResponse.fromDto(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.info("DELETE /api/tasks/{} - Deleting task", id);
        deleteTaskUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
