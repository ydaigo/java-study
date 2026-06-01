package com.example.demo.infrastructure.repository;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.model.TaskId;
import com.example.demo.domain.repository.TaskRepository;
import com.example.demo.infrastructure.entity.TaskJpaEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * タスクリポジトリ実装（Infrastructure層）
 */
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskJpaRepository jpaRepository;

    public TaskRepositoryImpl(TaskJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Task save(Task task) {
        TaskJpaEntity entity = TaskJpaEntity.fromDomain(task);
        TaskJpaEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Task> findById(TaskId id) {
        if (id.getValue() == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id.getValue())
                .map(TaskJpaEntity::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findAll().stream()
                .map(TaskJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByCompleted(boolean completed) {
        return jpaRepository.findByCompleted(completed).stream()
                .map(TaskJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByTitleContaining(String keyword) {
        return jpaRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(TaskJpaEntity::toDomain)
                .toList();
    }

    @Override
    public void delete(Task task) {
        if (task.getId().getValue() != null) {
            jpaRepository.deleteById(task.getId().getValue());
        }
    }

    @Override
    public boolean existsById(TaskId id) {
        if (id.getValue() == null) {
            return false;
        }
        return jpaRepository.existsById(id.getValue());
    }
}
