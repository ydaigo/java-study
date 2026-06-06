package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.domain.model.Task;
import com.example.demo.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateTaskUseCase のテスト")
class CreateTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private CreateTaskUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateTaskUseCase(taskRepository);
    }

    @Test
    @DisplayName("正常にタスクが作成されること")
    void shouldCreateTaskSuccessfully() {
        // Arrange
        String title = "新しいタスク";
        String description = "説明文";

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.assignId(1L);
            return task;
        });

        // Act
        TaskDto result = useCase.execute(title, description);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(title, result.title());
        assertEquals(description, result.description());
        assertFalse(result.completed());
        assertNotNull(result.createdAt());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("タイトルが空の場合に例外がスローされること")
    void shouldThrowExceptionWhenTitleIsEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute("", "説明文"));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("説明文がnullでもタスクが作成されること")
    void shouldCreateTaskWithNullDescription() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.assignId(1L);
            return task;
        });

        // Act
        TaskDto result = useCase.execute("タスク", null);

        // Assert
        assertNotNull(result);
        assertNull(result.description());
    }
}
