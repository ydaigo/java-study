package com.example.demo.presentation.controller;

import com.example.demo.application.usecase.CreateTaskUseCase;
import com.example.demo.application.usecase.DeleteTaskUseCase;
import com.example.demo.application.usecase.GetTaskUseCase;
import com.example.demo.application.usecase.UpdateTaskUseCase;
import com.example.demo.presentation.request.CreateTaskRequest;
import com.example.demo.presentation.request.UpdateTaskRequest;
import com.example.demo.presentation.response.TaskResponse;
import jakarta.validation.Valid;
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
        return getTaskUseCase.findAll().stream()
                .map(TaskResponse::fromDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return getTaskUseCase.findById(id)
                .map(TaskResponse::fromDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status")
    public List<TaskResponse> getTasksByStatus(@RequestParam boolean completed) {
        return getTaskUseCase.findByCompleted(completed).stream()
                .map(TaskResponse::fromDto)
                .toList();
    }

    @GetMapping("/search")
    public List<TaskResponse> searchTasks(@RequestParam String keyword) {
        return getTaskUseCase.search(keyword).stream()
                .map(TaskResponse::fromDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        var dto = createTaskUseCase.execute(request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.fromDto(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return updateTaskUseCase.execute(id, request.title(), request.description(), request.completed())
                .map(TaskResponse::fromDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (deleteTaskUseCase.execute(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
