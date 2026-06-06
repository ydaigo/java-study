package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("UpdateTaskUseCase のテスト")
class UpdateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private UpdateTaskUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateTaskUseCase(taskRepository);
    }

    @Nested
    @DisplayName("正常系")
    class Success {

        @Test
        @DisplayName("タイトルが更新されること")
        void shouldUpdateTitle() {
            // Arrange
            Task existingTask = Task.reconstruct(1L, "元のタイトル", "説明", false, LocalDateTime.now());
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            TaskDto result = useCase.execute(1L, "新しいタイトル", null, null);

            // Assert
            assertEquals("新しいタイトル", result.title());
            verify(taskRepository).save(any(Task.class));
        }

        @Test
        @DisplayName("説明文が更新されること")
        void shouldUpdateDescription() {
            // Arrange
            Task existingTask = Task.reconstruct(1L, "タイトル", "元の説明", false, LocalDateTime.now());
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            TaskDto result = useCase.execute(1L, null, "新しい説明", null);

            // Assert
            assertEquals("新しい説明", result.description());
        }

        @Test
        @DisplayName("完了状態が更新されること")
        void shouldUpdateCompletedStatus() {
            // Arrange
            Task existingTask = Task.reconstruct(1L, "タイトル", "説明", false, LocalDateTime.now());
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            TaskDto result = useCase.execute(1L, null, null, true);

            // Assert
            assertTrue(result.completed());
        }

        @Test
        @DisplayName("未完了状態に戻せること")
        void shouldUncompleteTask() {
            // Arrange
            Task existingTask = Task.reconstruct(1L, "タイトル", "説明", true, LocalDateTime.now());
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            TaskDto result = useCase.execute(1L, null, null, false);

            // Assert
            assertFalse(result.completed());
        }

        @Test
        @DisplayName("全フィールドが同時に更新されること")
        void shouldUpdateAllFields() {
            // Arrange
            Task existingTask = Task.reconstruct(1L, "元のタイトル", "元の説明", false, LocalDateTime.now());
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            TaskDto result = useCase.execute(1L, "新タイトル", "新説明", true);

            // Assert
            assertEquals("新タイトル", result.title());
            assertEquals("新説明", result.description());
            assertTrue(result.completed());
        }
    }

    @Nested
    @DisplayName("異常系")
    class Failure {

        @Test
        @DisplayName("存在しないIDで例外がスローされること")
        void shouldThrowExceptionWhenTaskNotFound() {
            // Arrange
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.empty());

            // Act & Assert
            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> useCase.execute(999L, "タイトル", null, null)
            );

            assertTrue(exception.getMessage().contains("999"));
            verify(taskRepository, never()).save(any(Task.class));
        }

        @Test
        @DisplayName("空のタイトルで例外がスローされること")
        void shouldThrowExceptionWhenTitleIsEmpty() {
            // Arrange
            Task existingTask = Task.reconstruct(1L, "元のタイトル", "説明", false, LocalDateTime.now());
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(existingTask));

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> useCase.execute(1L, "", null, null));

            verify(taskRepository, never()).save(any(Task.class));
        }
    }
}
