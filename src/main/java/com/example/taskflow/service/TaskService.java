package com.example.taskflow.service;

import com.example.taskflow.dto.TaskRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.entity.*;
import com.example.taskflow.exception.NotFoundException;
import com.example.taskflow.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return toResponse(taskRepo.save(task));
    }

    public List<TaskResponse> getAll() {
        return taskRepo.findAll().stream().map(this::toResponse).toList();
    }

    public TaskResponse getById(Long id) {
        return toResponse(find(id));
    }

    public TaskResponse update(Long id, TaskRequest req) {
        Task task = find(id);
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setDeadline(req.deadline());
        return toResponse(taskRepo.save(task));
    }

    public void delete(Long id) {
        taskRepo.delete(find(id));
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