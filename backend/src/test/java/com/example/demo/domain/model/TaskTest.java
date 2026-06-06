package com.example.demo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Task ドメインモデルのテスト")
class TaskTest {

    @Nested
    @DisplayName("create() メソッド")
    class Create {

        @Test
        @DisplayName("正常にタスクが作成されること")
        void shouldCreateTaskSuccessfully() {
            Task task = Task.create("テストタスク", "説明文");

            assertNotNull(task);
            assertEquals("テストタスク", task.getTitle());
            assertEquals("説明文", task.getDescription());
            assertFalse(task.isCompleted());
            assertNotNull(task.getCreatedAt());
            assertNull(task.getId().getValue());
        }

        @Test
        @DisplayName("タイトルがnullの場合に例外がスローされること")
        void shouldThrowExceptionWhenTitleIsNull() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Task.create(null, "説明文")
            );
            assertEquals("Title is required", exception.getMessage());
        }

        @Test
        @DisplayName("タイトルが空文字の場合に例外がスローされること")
        void shouldThrowExceptionWhenTitleIsEmpty() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Task.create("", "説明文")
            );
            assertEquals("Title is required", exception.getMessage());
        }

        @Test
        @DisplayName("タイトルが空白のみの場合に例外がスローされること")
        void shouldThrowExceptionWhenTitleIsBlank() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Task.create("   ", "説明文")
            );
            assertEquals("Title is required", exception.getMessage());
        }

        @Test
        @DisplayName("タイトルが256文字以上の場合に例外がスローされること")
        void shouldThrowExceptionWhenTitleExceeds255Characters() {
            String longTitle = "a".repeat(256);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> Task.create(longTitle, "説明文")
            );
            assertEquals("Title must be 255 characters or less", exception.getMessage());
        }

        @Test
        @DisplayName("タイトルが255文字の場合は正常に作成されること")
        void shouldCreateTaskWhenTitleIs255Characters() {
            String maxLengthTitle = "a".repeat(255);

            Task task = Task.create(maxLengthTitle, "説明文");

            assertNotNull(task);
            assertEquals(255, task.getTitle().length());
        }

        @Test
        @DisplayName("説明文がnullでも正常に作成されること")
        void shouldCreateTaskWhenDescriptionIsNull() {
            Task task = Task.create("テストタスク", null);

            assertNotNull(task);
            assertNull(task.getDescription());
        }
    }

    @Nested
    @DisplayName("reconstruct() メソッド")
    class Reconstruct {

        @Test
        @DisplayName("永続化データからタスクが復元されること")
        void shouldReconstructTaskFromPersistedData() {
            Task task = Task.reconstruct(1L, "タスク", "説明", true,
                    java.time.LocalDateTime.of(2024, 1, 1, 12, 0));

            assertEquals(1L, task.getId().getValue());
            assertEquals("タスク", task.getTitle());
            assertEquals("説明", task.getDescription());
            assertTrue(task.isCompleted());
        }
    }

    @Nested
    @DisplayName("changeTitle() メソッド")
    class ChangeTitle {

        @Test
        @DisplayName("タイトルが正常に変更されること")
        void shouldChangeTitleSuccessfully() {
            Task task = Task.create("元のタイトル", "説明文");

            task.changeTitle("新しいタイトル");

            assertEquals("新しいタイトル", task.getTitle());
        }

        @Test
        @DisplayName("空のタイトルに変更しようとすると例外がスローされること")
        void shouldThrowExceptionWhenChangingToEmptyTitle() {
            Task task = Task.create("元のタイトル", "説明文");

            assertThrows(IllegalArgumentException.class, () -> task.changeTitle(""));
        }
    }

    @Nested
    @DisplayName("changeDescription() メソッド")
    class ChangeDescription {

        @Test
        @DisplayName("説明文が正常に変更されること")
        void shouldChangeDescriptionSuccessfully() {
            Task task = Task.create("タイトル", "元の説明文");

            task.changeDescription("新しい説明文");

            assertEquals("新しい説明文", task.getDescription());
        }

        @Test
        @DisplayName("説明文をnullに変更できること")
        void shouldChangeDescriptionToNull() {
            Task task = Task.create("タイトル", "元の説明文");

            task.changeDescription(null);

            assertNull(task.getDescription());
        }
    }

    @Nested
    @DisplayName("complete() / uncomplete() メソッド")
    class CompletionStatus {

        @Test
        @DisplayName("タスクを完了状態にできること")
        void shouldCompleteTask() {
            Task task = Task.create("タイトル", "説明文");
            assertFalse(task.isCompleted());

            task.complete();

            assertTrue(task.isCompleted());
        }

        @Test
        @DisplayName("タスクを未完了状態に戻せること")
        void shouldUncompleteTask() {
            Task task = Task.create("タイトル", "説明文");
            task.complete();
            assertTrue(task.isCompleted());

            task.uncomplete();

            assertFalse(task.isCompleted());
        }
    }

    @Nested
    @DisplayName("equals() / hashCode() メソッド")
    class Equality {

        @Test
        @DisplayName("同じIDを持つタスクは等しいと判定されること")
        void shouldBeEqualWhenSameId() {
            Task task1 = Task.reconstruct(1L, "タスク1", "説明1", false,
                    java.time.LocalDateTime.now());
            Task task2 = Task.reconstruct(1L, "タスク2", "説明2", true,
                    java.time.LocalDateTime.now());

            assertEquals(task1, task2);
            assertEquals(task1.hashCode(), task2.hashCode());
        }

        @Test
        @DisplayName("異なるIDを持つタスクは等しくないと判定されること")
        void shouldNotBeEqualWhenDifferentId() {
            Task task1 = Task.reconstruct(1L, "タスク", "説明", false,
                    java.time.LocalDateTime.now());
            Task task2 = Task.reconstruct(2L, "タスク", "説明", false,
                    java.time.LocalDateTime.now());

            assertNotEquals(task1, task2);
        }
    }
}
