package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * タスクリポジトリ実装（JdbcTemplate使用）
 */
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Task> taskRowMapper = (rs, rowNum) ->
            Task.reconstruct(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getBoolean("completed"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task save(Task task) {
        if (task.getId().getValue() == null) {
            return insert(task);
        } else {
            return update(task);
        }
    }

    private Task insert(Task task) {
        String sql = "INSERT INTO tasks (title, description, completed, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setBoolean(3, task.isCompleted());
            ps.setTimestamp(4, Timestamp.valueOf(task.getCreatedAt()));
            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();
        if (generatedId == null) {
            throw new IllegalStateException("Failed to retrieve generated ID for task");
        }
        task.assignId(generatedId.longValue());
        return task;
    }

    private Task update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, completed = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getId().getValue()
        );
        return task;
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        if (id.getValue() == null) {
            return Optional.empty();
        }
        String sql = "SELECT id, title, description, completed, created_at FROM tasks WHERE id = ?";
        List<Task> tasks = jdbcTemplate.query(sql, taskRowMapper, id.getValue());
        return tasks.isEmpty() ? Optional.empty() : Optional.of(tasks.get(0));
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT id, title, description, completed, created_at FROM tasks ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, taskRowMapper);
    }

    @Override
    public List<Task> findByCompleted(boolean completed) {
        String sql = "SELECT id, title, description, completed, created_at FROM tasks WHERE completed = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, taskRowMapper, completed);
    }

    @Override
    public List<Task> findByTitleContaining(String keyword) {
        String sql = "SELECT id, title, description, completed, created_at FROM tasks WHERE LOWER(title) LIKE LOWER(?) ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, taskRowMapper, "%" + keyword + "%");
    }

    @Override
    public void delete(Task task) {
        if (task.getId().getValue() != null) {
            String sql = "DELETE FROM tasks WHERE id = ?";
            jdbcTemplate.update(sql, task.getId().getValue());
        }
    }

    @Override
    public boolean existsById(TaskId id) {
        if (id.getValue() == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM tasks WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id.getValue());
        return count != null && count > 0;
    }
}
