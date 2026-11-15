package com.example.taskflow.service;

import com.example.taskflow.dto.TaskRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.entity.*;
import com.example.taskflow.exception.NotFoundException;
import com.example.taskflow.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public TaskResponse create(TaskRequest req) {
        Project project = projectRepo.findById(req.projectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));
        User assignee = null;
        if (req.assigneeId() != null) {
            assignee = userRepo.findById(req.assigneeId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        }
        Task task = Task.builder()
                .title(req.title())
                .description(req.description())
                .deadline(req.deadline())
                .status(TaskStatus.TODO)
                .project(project)
                .assignee(assignee)
                .build();
        Task saved = taskRepo.save(task);
        log.info("Task created: id={}, title={}", saved.getId(), saved.getTitle());
        return toResponse(saved);
    }

    public List<TaskResponse> getAll() {
        return taskRepo.findAll().stream().map(this::toResponse).toList();
    }

    public Page<TaskResponse> search(String title, TaskStatus status, Long projectId, Pageable pageable) {
        String t = (title == null || title.isBlank()) ? null : title.trim();
        return taskRepo.search(t, status, projectId, pageable).map(this::toResponse);
    }

    public TaskResponse getById(Long id) {
        return toResponse(find(id));
    }

    public TaskResponse update(Long id, TaskRequest req) {
        Task task = find(id);
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setDeadline(req.deadline());
        log.info("Task updated: id={}", id);
        return toResponse(taskRepo.save(task));
    }

    public void delete(Long id) {
        taskRepo.delete(find(id));
        log.info("Task deleted: id={}", id);
    }

    private Task find(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId(), t.getTitle(), t.getDescription(),
                t.getStatus().name(), t.getDeadline(),
                t.getProject() != null ? t.getProject().getId() : null,
                t.getAssignee() != null ? t.getAssignee().getId() : null
        );
    }
}