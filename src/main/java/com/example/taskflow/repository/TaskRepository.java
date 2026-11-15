package com.example.taskflow.repository;

import com.example.taskflow.entity.Task;
import com.example.taskflow.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByAssigneeId(Long assigneeId);

    @Query("""
            SELECT t FROM Task t
            WHERE (:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', CAST(:title AS string), '%')))
              AND (:status IS NULL OR t.status = :status)
              AND (:projectId IS NULL OR t.project.id = :projectId)
            """)
    Page<Task> search(@Param("title") String title,
                      @Param("status") TaskStatus status,
                      @Param("projectId") Long projectId,
                      Pageable pageable);
}