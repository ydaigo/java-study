package com.example.demo.presentation.controller;

import com.example.demo.application.dto.TaskDto;
import com.example.demo.application.usecase.CreateTaskUseCase;
import com.example.demo.application.usecase.DeleteTaskUseCase;
import com.example.demo.application.usecase.GetTaskUseCase;
import com.example.demo.application.usecase.UpdateTaskUseCase;
import com.example.demo.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@DisplayName("TaskController 統合テスト")
@SuppressWarnings("null")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateTaskUseCase createTaskUseCase;

    @MockitoBean
    private GetTaskUseCase getTaskUseCase;

    @MockitoBean
    private UpdateTaskUseCase updateTaskUseCase;

    @MockitoBean
    private DeleteTaskUseCase deleteTaskUseCase;

    @Nested
    @DisplayName("GET /api/tasks")
    class GetAllTasks {

        @Test
        @DisplayName("一覧取得が成功すること")
        void shouldReturnAllTasks() throws Exception {
            // Arrange
            List<TaskDto> tasks = List.of(
                    new TaskDto(1L, "タスク1", "説明1", false, LocalDateTime.now()),
                    new TaskDto(2L, "タスク2", "説明2", true, LocalDateTime.now())
            );
            when(getTaskUseCase.findAll()).thenReturn(tasks);

            // Act & Assert
            mockMvc.perform(get("/api/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].title", is("タスク1")))
                    .andExpect(jsonPath("$[1].title", is("タスク2")));
        }

        @Test
        @DisplayName("タスクがない場合は空配列が返ること")
        void shouldReturnEmptyArrayWhenNoTasks() throws Exception {
            // Arrange
            when(getTaskUseCase.findAll()).thenReturn(List.of());

            // Act & Assert
            mockMvc.perform(get("/api/tasks"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/tasks/{id}")
    class GetTaskById {

        @Test
        @DisplayName("IDでタスクが取得できること")
        void shouldReturnTaskById() throws Exception {
            // Arrange
            TaskDto task = new TaskDto(1L, "タスク", "説明", false, LocalDateTime.now());
            when(getTaskUseCase.findById(1L)).thenReturn(Optional.of(task));

            // Act & Assert
            mockMvc.perform(get("/api/tasks/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.title", is("タスク")));
        }

        @Test
        @DisplayName("存在しないIDで404が返ること")
        void shouldReturn404WhenNotFound() throws Exception {
            // Arrange
            when(getTaskUseCase.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            mockMvc.perform(get("/api/tasks/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)));
        }
    }

    @Nested
    @DisplayName("GET /api/tasks/status")
    class GetTasksByStatus {

        @Test
        @DisplayName("完了状態でフィルタできること")
        void shouldFilterByCompletedStatus() throws Exception {
            // Arrange
            List<TaskDto> completedTasks = List.of(
                    new TaskDto(1L, "完了タスク", "説明", true, LocalDateTime.now())
            );
            when(getTaskUseCase.findByCompleted(true)).thenReturn(completedTasks);

            // Act & Assert
            mockMvc.perform(get("/api/tasks/status").param("completed", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].completed", is(true)));
        }
    }

    @Nested
    @DisplayName("GET /api/tasks/search")
    class SearchTasks {

        @Test
        @DisplayName("キーワードで検索できること")
        void shouldSearchByKeyword() throws Exception {
            // Arrange
            List<TaskDto> tasks = List.of(
                    new TaskDto(1L, "会議の準備", "説明", false, LocalDateTime.now())
            );
            when(getTaskUseCase.search("会議")).thenReturn(tasks);

            // Act & Assert
            mockMvc.perform(get("/api/tasks/search").param("keyword", "会議"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].title", containsString("会議")));
        }
    }

    @Nested
    @DisplayName("POST /api/tasks")
    class CreateTask {

        @Test
        @DisplayName("タスクが作成されること")
        void shouldCreateTask() throws Exception {
            // Arrange
            TaskDto createdTask = new TaskDto(1L, "新しいタスク", "説明文", false, LocalDateTime.now());
            when(createTaskUseCase.execute("新しいタスク", "説明文")).thenReturn(createdTask);

            String requestBody = """
                {
                    "title": "新しいタスク",
                    "description": "説明文"
                }
                """;

            // Act & Assert
            mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.title", is("新しいタスク")));
        }

        @Test
        @DisplayName("タイトルが空の場合に400が返ること")
        void shouldReturn400WhenTitleIsBlank() throws Exception {
            String requestBody = """
                {
                    "title": "",
                    "description": "説明文"
                }
                """;

            // Act & Assert
            mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status", is(400)));
        }

        @Test
        @DisplayName("タイトルがnullの場合に400が返ること")
        void shouldReturn400WhenTitleIsNull() throws Exception {
            String requestBody = """
                {
                    "description": "説明文"
                }
                """;

            // Act & Assert
            mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("タイトルが256文字以上の場合に400が返ること")
        void shouldReturn400WhenTitleTooLong() throws Exception {
            String longTitle = "a".repeat(256);
            String requestBody = String.format("""
                {
                    "title": "%s",
                    "description": "説明文"
                }
                """, longTitle);

            // Act & Assert
            mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/tasks/{id}")
    class UpdateTask {

        @Test
        @DisplayName("タスクが更新されること")
        void shouldUpdateTask() throws Exception {
            // Arrange
            TaskDto updatedTask = new TaskDto(1L, "更新後のタスク", "更新後の説明", true, LocalDateTime.now());
            when(updateTaskUseCase.execute(eq(1L), any(), any(), any())).thenReturn(updatedTask);

            String requestBody = """
                {
                    "title": "更新後のタスク",
                    "description": "更新後の説明",
                    "completed": true
                }
                """;

            // Act & Assert
            mockMvc.perform(put("/api/tasks/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("更新後のタスク")))
                    .andExpect(jsonPath("$.completed", is(true)));
        }

        @Test
        @DisplayName("存在しないIDで404が返ること")
        void shouldReturn404WhenNotFound() throws Exception {
            // Arrange
            when(updateTaskUseCase.execute(eq(999L), any(), any(), any()))
                    .thenThrow(new ResourceNotFoundException("Task", "id", 999L));

            String requestBody = """
                {
                    "title": "タスク"
                }
                """;

            // Act & Assert
            mockMvc.perform(put("/api/tasks/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/tasks/{id}")
    class DeleteTask {

        @Test
        @DisplayName("タスクが削除されること")
        void shouldDeleteTask() throws Exception {
            // Arrange
            doNothing().when(deleteTaskUseCase).execute(1L);

            // Act & Assert
            mockMvc.perform(delete("/api/tasks/1"))
                    .andExpect(status().isNoContent());

            verify(deleteTaskUseCase).execute(1L);
        }

        @Test
        @DisplayName("存在しないIDで404が返ること")
        void shouldReturn404WhenNotFound() throws Exception {
            // Arrange
            doThrow(new ResourceNotFoundException("Task", "id", 999L))
                    .when(deleteTaskUseCase).execute(999L);

            // Act & Assert
            mockMvc.perform(delete("/api/tasks/999"))
                    .andExpect(status().isNotFound());
        }
    }
}
