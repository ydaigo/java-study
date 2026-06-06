package com.example.demo.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskId 値オブジェクトのテスト")
class TaskIdTest {

    @Nested
    @DisplayName("コンストラクタ")
    class Constructor {

        @Test
        @DisplayName("正の数値で正常に作成されること")
        void shouldCreateWithPositiveValue() {
            TaskId taskId = new TaskId(1L);

            assertEquals(1L, taskId.getValue());
        }

        @Test
        @DisplayName("nullで正常に作成されること（新規作成時）")
        void shouldCreateWithNullValue() {
            TaskId taskId = new TaskId(null);

            assertNull(taskId.getValue());
        }

        @Test
        @DisplayName("0で例外がスローされること")
        void shouldThrowExceptionWhenZero() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TaskId(0L)
            );
            assertEquals("TaskId must be positive", exception.getMessage());
        }

        @Test
        @DisplayName("負の数値で例外がスローされること")
        void shouldThrowExceptionWhenNegative() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new TaskId(-1L)
            );
            assertEquals("TaskId must be positive", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("equals() / hashCode() メソッド")
    class Equality {

        @Test
        @DisplayName("同じ値を持つTaskIdは等しいと判定されること")
        void shouldBeEqualWhenSameValue() {
            TaskId taskId1 = new TaskId(1L);
            TaskId taskId2 = new TaskId(1L);

            assertEquals(taskId1, taskId2);
            assertEquals(taskId1.hashCode(), taskId2.hashCode());
        }

        @Test
        @DisplayName("異なる値を持つTaskIdは等しくないと判定されること")
        void shouldNotBeEqualWhenDifferentValue() {
            TaskId taskId1 = new TaskId(1L);
            TaskId taskId2 = new TaskId(2L);

            assertNotEquals(taskId1, taskId2);
        }

        @Test
        @DisplayName("両方nullの場合は等しいと判定されること")
        void shouldBeEqualWhenBothNull() {
            TaskId taskId1 = new TaskId(null);
            TaskId taskId2 = new TaskId(null);

            assertEquals(taskId1, taskId2);
        }
    }
}
