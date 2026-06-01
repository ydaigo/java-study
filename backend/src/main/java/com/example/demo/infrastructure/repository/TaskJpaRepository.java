package com.example.demo.infrastructure.repository;

import com.example.demo.infrastructure.entity.TaskJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Spring Data JPA リポジトリ（Infrastructure層）
 */
public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, Long> {

    List<TaskJpaEntity> findByCompleted(boolean completed);

    List<TaskJpaEntity> findByTitleContainingIgnoreCase(String keyword);
}
