package com.example.demo.application.usecase;

import com.example.demo.application.dto.TaskDto;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetTaskUseCase のテスト")
class GetTaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private GetTaskUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetTaskUseCase(taskRepository);
    }

    @Nested
    @DisplayName("findAll() メソッド")
    class FindAll {

        @Test
        @DisplayName("全件取得できること")
        void shouldReturnAllTasks() {
            // Arrange
            List<Task> tasks = List.of(
                    Task.reconstruct(1L, "タスク1", "説明1", false, LocalDateTime.now()),
                    Task.reconstruct(2L, "タスク2", "説明2", true, LocalDateTime.now())
            );
            when(taskRepository.findAll()).thenReturn(tasks);

            // Act
            List<TaskDto> result = useCase.findAll();

            // Assert
            assertEquals(2, result.size());
            assertEquals("タスク1", result.get(0).title());
            assertEquals("タスク2", result.get(1).title());
        }

        @Test
        @DisplayName("タスクがない場合は空のリストが返ること")
        void shouldReturnEmptyListWhenNoTasks() {
            // Arrange
            when(taskRepository.findAll()).thenReturn(List.of());

            // Act
            List<TaskDto> result = useCase.findAll();

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findById() メソッド")
    class FindById {

        @Test
        @DisplayName("存在するIDでタスクが返ること")
        void shouldReturnTaskWhenExists() {
            // Arrange
            Task task = Task.reconstruct(1L, "タスク", "説明", false, LocalDateTime.now());
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.of(task));

            // Act
            Optional<TaskDto> result = useCase.findById(1L);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(1L, result.get().id());
            assertEquals("タスク", result.get().title());
        }

        @Test
        @DisplayName("存在しないIDでOptionalが空であること")
        void shouldReturnEmptyWhenNotExists() {
            // Arrange
            when(taskRepository.findById(any(TaskId.class))).thenReturn(Optional.empty());

            // Act
            Optional<TaskDto> result = useCase.findById(999L);

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByCompleted() メソッド")
    class FindByCompleted {

        @Test
        @DisplayName("完了状態でフィルタできること")
        void shouldFilterByCompletedStatus() {
            // Arrange
            List<Task> completedTasks = List.of(
                    Task.reconstruct(1L, "完了タスク", "説明", true, LocalDateTime.now())
            );
            when(taskRepository.findByCompleted(true)).thenReturn(completedTasks);

            // Act
            List<TaskDto> result = useCase.findByCompleted(true);

            // Assert
            assertEquals(1, result.size());
            assertTrue(result.get(0).completed());
        }

        @Test
        @DisplayName("未完了状態でフィルタできること")
        void shouldFilterByIncompleteStatus() {
            // Arrange
            List<Task> incompleteTasks = List.of(
                    Task.reconstruct(1L, "未完了タスク1", "説明", false, LocalDateTime.now()),
                    Task.reconstruct(2L, "未完了タスク2", "説明", false, LocalDateTime.now())
            );
            when(taskRepository.findByCompleted(false)).thenReturn(incompleteTasks);

            // Act
            List<TaskDto> result = useCase.findByCompleted(false);

            // Assert
            assertEquals(2, result.size());
            assertFalse(result.get(0).completed());
        }
    }

    @Nested
    @DisplayName("search() メソッド")
    class Search {

        @Test
        @DisplayName("キーワードで検索できること")
        void shouldSearchByKeyword() {
            // Arrange
            List<Task> matchingTasks = List.of(
                    Task.reconstruct(1L, "会議の準備", "説明", false, LocalDateTime.now()),
                    Task.reconstruct(2L, "会議の議事録", "説明", false, LocalDateTime.now())
            );
            when(taskRepository.findByTitleContaining("会議")).thenReturn(matchingTasks);

            // Act
            List<TaskDto> result = useCase.search("会議");

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.get(0).title().contains("会議"));
        }

        @Test
        @DisplayName("マッチしない場合は空のリストが返ること")
        void shouldReturnEmptyListWhenNoMatch() {
            // Arrange
            when(taskRepository.findByTitleContaining("存在しない")).thenReturn(List.of());

            // Act
            List<TaskDto> result = useCase.search("存在しない");

            // Assert
            assertTrue(result.isEmpty());
        }
    }
}
