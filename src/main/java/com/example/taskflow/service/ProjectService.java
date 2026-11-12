package com.example.taskflow.service;

import com.example.taskflow.dto.ProjectRequest;
import com.example.taskflow.dto.ProjectResponse;
import com.example.taskflow.entity.Project;
import com.example.taskflow.entity.User;
import com.example.taskflow.exception.NotFoundException;
import com.example.taskflow.repository.ProjectRepository;
import com.example.taskflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final UserRepository userRepo;

    public ProjectResponse create(ProjectRequest req) {
        User owner = null;
        if (req.ownerId() != null) {
            owner = userRepo.findById(req.ownerId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
        }
        Project project = Project.builder()
                .name(req.name())
                .description(req.description())
                .owner(owner)
                .build();
        return toResponse(projectRepo.save(project));
    }

    public List<ProjectResponse> getAll() {
        return projectRepo.findAll().stream().map(this::toResponse).toList();
    }

    public ProjectResponse getById(Long id) {
        return toResponse(find(id));
    }

    public ProjectResponse update(Long id, ProjectRequest req) {
        Project project = find(id);
        project.setName(req.name());
        project.setDescription(req.description());
        return toResponse(projectRepo.save(project));
    }

    public void delete(Long id) {
        projectRepo.delete(find(id));
    }

    private Project find(Long id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }

    private ProjectResponse toResponse(Project p) {
        return new ProjectResponse(
                p.getId(), p.getName(), p.getDescription(),
                p.getOwner() != null ? p.getOwner().getId() : null
        );
    }
}
