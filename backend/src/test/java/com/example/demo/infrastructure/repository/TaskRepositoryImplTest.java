package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(TaskRepositoryImpl.class)
@ActiveProfiles("test")
@DisplayName("TaskRepositoryImpl 統合テスト")
class TaskRepositoryImplTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM tasks");
    }

    @Nested
    @DisplayName("save() メソッド")
    class Save {

        @Test
        @DisplayName("新規タスクが保存されること")
        void shouldSaveNewTask() {
            // Arrange
            Task task = Task.create("新しいタスク", "説明文");

            // Act
            Task savedTask = taskRepository.save(task);

            // Assert
            assertNotNull(savedTask.getId().getValue());
            assertEquals("新しいタスク", savedTask.getTitle());
            assertEquals("説明文", savedTask.getDescription());
            assertFalse(savedTask.isCompleted());
        }

        @Test
        @DisplayName("既存タスクが更新されること")
        void shouldUpdateExistingTask() {
            // Arrange
            Task task = Task.create("元のタスク", "元の説明");
            Task savedTask = taskRepository.save(task);
            Long taskId = savedTask.getId().getValue();

            savedTask.changeTitle("更新後のタスク");
            savedTask.changeDescription("更新後の説明");
            savedTask.complete();

            // Act
            Task updatedTask = taskRepository.save(savedTask);

            // Assert
            assertEquals(taskId, updatedTask.getId().getValue());
            assertEquals("更新後のタスク", updatedTask.getTitle());
            assertEquals("更新後の説明", updatedTask.getDescription());
            assertTrue(updatedTask.isCompleted());
        }
    }

    @Nested
    @DisplayName("findById() メソッド")
    class FindById {

        @Test
        @DisplayName("存在するIDでタスクが取得できること")
        void shouldFindTaskById() {
            // Arrange
            Task task = Task.create("テストタスク", "説明");
            Task savedTask = taskRepository.save(task);

            // Act
            Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

            // Assert
            assertTrue(foundTask.isPresent());
            assertEquals(savedTask.getId().getValue(), foundTask.get().getId().getValue());
            assertEquals("テストタスク", foundTask.get().getTitle());
        }

        @Test
        @DisplayName("存在しないIDでOptionalが空であること")
        void shouldReturnEmptyWhenNotFound() {
            // Act
            Optional<Task> foundTask = taskRepository.findById(new TaskId(999L));

            // Assert
            assertTrue(foundTask.isEmpty());
        }

        @Test
        @DisplayName("nullのIDでOptionalが空であること")
        void shouldReturnEmptyWhenIdIsNull() {
            // Act
            Optional<Task> foundTask = taskRepository.findById(new TaskId(null));

            // Assert
            assertTrue(foundTask.isEmpty());
        }
    }

    @Nested
    @DisplayName("findAll() メソッド")
    class FindAll {

        @Test
        @DisplayName("全件取得できること")
        void shouldFindAllTasks() {
            // Arrange
            taskRepository.save(Task.create("タスク1", "説明1"));
            taskRepository.save(Task.create("タスク2", "説明2"));
            taskRepository.save(Task.create("タスク3", "説明3"));

            // Act
            List<Task> tasks = taskRepository.findAll();

            // Assert
            assertEquals(3, tasks.size());
        }

        @Test
        @DisplayName("タスクがない場合は空のリストが返ること")
        void shouldReturnEmptyListWhenNoTasks() {
            // Act
            List<Task> tasks = taskRepository.findAll();

            // Assert
            assertTrue(tasks.isEmpty());
        }
    }

    @Nested
    @DisplayName("findByCompleted() メソッド")
    class FindByCompleted {

        @Test
        @DisplayName("完了タスクのみ取得できること")
        void shouldFindCompletedTasks() {
            // Arrange
            Task task1 = Task.create("未完了タスク", "説明");
            taskRepository.save(task1);

            Task task2 = Task.create("完了タスク", "説明");
            task2.complete();
            taskRepository.save(task2);

            // Act
            List<Task> completedTasks = taskRepository.findByCompleted(true);

            // Assert
            assertEquals(1, completedTasks.size());
            assertTrue(completedTasks.get(0).isCompleted());
            assertEquals("完了タスク", completedTasks.get(0).getTitle());
        }

        @Test
        @DisplayName("未完了タスクのみ取得できること")
        void shouldFindIncompleteTasks() {
            // Arrange
            Task task1 = Task.create("未完了タスク", "説明");
            taskRepository.save(task1);

            Task task2 = Task.create("完了タスク", "説明");
            task2.complete();
            taskRepository.save(task2);

            // Act
            List<Task> incompleteTasks = taskRepository.findByCompleted(false);

            // Assert
            assertEquals(1, incompleteTasks.size());
            assertFalse(incompleteTasks.get(0).isCompleted());
        }
    }

    @Nested
    @DisplayName("findByTitleContaining() メソッド")
    class FindByTitleContaining {

        @Test
        @DisplayName("キーワードを含むタスクが取得できること")
        void shouldFindTasksByKeyword() {
            // Arrange
            taskRepository.save(Task.create("会議の準備", "説明"));
            taskRepository.save(Task.create("会議の議事録", "説明"));
            taskRepository.save(Task.create("買い物リスト", "説明"));

            // Act
            List<Task> tasks = taskRepository.findByTitleContaining("会議");

            // Assert
            assertEquals(2, tasks.size());
            assertTrue(tasks.stream().allMatch(t -> t.getTitle().contains("会議")));
        }

        @Test
        @DisplayName("大文字小文字を区別せずに検索できること")
        void shouldSearchCaseInsensitive() {
            // Arrange
            taskRepository.save(Task.create("Meeting Notes", "説明"));
            taskRepository.save(Task.create("MEETING AGENDA", "説明"));

            // Act
            List<Task> tasks = taskRepository.findByTitleContaining("meeting");

            // Assert
            assertEquals(2, tasks.size());
        }

        @Test
        @DisplayName("マッチしない場合は空のリストが返ること")
        void shouldReturnEmptyListWhenNoMatch() {
            // Arrange
            taskRepository.save(Task.create("タスク", "説明"));

            // Act
            List<Task> tasks = taskRepository.findByTitleContaining("存在しないキーワード");

            // Assert
            assertTrue(tasks.isEmpty());
        }
    }

    @Nested
    @DisplayName("delete() メソッド")
    class Delete {

        @Test
        @DisplayName("タスクが削除されること")
        void shouldDeleteTask() {
            // Arrange
            Task task = Task.create("削除対象タスク", "説明");
            Task savedTask = taskRepository.save(task);
            TaskId taskId = savedTask.getId();

            // Act
            taskRepository.delete(savedTask);

            // Assert
            assertTrue(taskRepository.findById(taskId).isEmpty());
        }
    }

    @Nested
    @DisplayName("existsById() メソッド")
    class ExistsById {

        @Test
        @DisplayName("存在するIDでtrueが返ること")
        void shouldReturnTrueWhenExists() {
            // Arrange
            Task task = Task.create("タスク", "説明");
            Task savedTask = taskRepository.save(task);

            // Act
            boolean exists = taskRepository.existsById(savedTask.getId());

            // Assert
            assertTrue(exists);
        }

        @Test
        @DisplayName("存在しないIDでfalseが返ること")
        void shouldReturnFalseWhenNotExists() {
            // Act
            boolean exists = taskRepository.existsById(new TaskId(999L));

            // Assert
            assertFalse(exists);
        }

        @Test
        @DisplayName("nullのIDでfalseが返ること")
        void shouldReturnFalseWhenIdIsNull() {
            // Act
            boolean exists = taskRepository.existsById(new TaskId(null));

            // Assert
            assertFalse(exists);
        }
    }
}
