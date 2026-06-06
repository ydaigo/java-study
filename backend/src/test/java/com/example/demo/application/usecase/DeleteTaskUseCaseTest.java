package com.example.demo.application.usecase;

import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteTaskUseCase のテスト")
class DeleteTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private DeleteTaskUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteTaskUseCase(taskRepository);
    }

    @Test
    @DisplayName("正常にタスクが削除されること")
    void shouldDeleteTaskSuccessfully() {
        // Arrange
        Task existingTask = Task.reconstruct(1L, "タスク", "説明", false, LocalDateTime.now());
        when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(existingTask));
        doNothing().when(taskRepository).delete(any(Task.class));

        // Act
        assertDoesNotThrow(() -> useCase.execute(1L));

        // Assert
        verify(taskRepository).findById(any(TaskId.class));
        verify(taskRepository).delete(existingTask);
    }

    @Test
    @DisplayName("存在しないIDで例外がスローされること")
    void shouldThrowExceptionWhenTaskNotFound() {
        // Arrange
        when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> useCase.execute(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(taskRepository, never()).delete(any(Task.class));
    }
}
