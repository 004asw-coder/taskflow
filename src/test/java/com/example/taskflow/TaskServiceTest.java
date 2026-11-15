package com.example.taskflow;

import com.example.taskflow.dto.TaskRequest;
import com.example.taskflow.dto.TaskResponse;
import com.example.taskflow.entity.Project;
import com.example.taskflow.entity.Task;
import com.example.taskflow.entity.TaskStatus;
import com.example.taskflow.exception.NotFoundException;
import com.example.taskflow.repository.ProjectRepository;
import com.example.taskflow.repository.TaskRepository;
import com.example.taskflow.repository.UserRepository;
import com.example.taskflow.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepo;
    @Mock
    private ProjectRepository projectRepo;
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private TaskService service;

    @Test
    void create_savesTaskWithTodoStatus() {
        Project project = Project.builder().id(1L).name("P").build();
        when(projectRepo.findById(1L)).thenReturn(Optional.of(project));
        when(taskRepo.save(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            t.setId(10L);
            return t;
        });

        TaskRequest req = new TaskRequest("Test", "desc", 1L, null, LocalDate.of(2026, 12, 1));
        TaskResponse res = service.create(req);

        assertEquals(10L, res.id());
        assertEquals("Test", res.title());
        assertEquals("TODO", res.status());
        assertEquals(1L, res.projectId());
        verify(taskRepo).save(any(Task.class));
    }

    @Test
    void create_throwsWhenProjectNotFound() {
        when(projectRepo.findById(99L)).thenReturn(Optional.empty());
        TaskRequest req = new TaskRequest("Test", null, 99L, null, null);

        assertThrows(NotFoundException.class, () -> service.create(req));
        verify(taskRepo, never()).save(any());
    }

    @Test
    void getById_returnsTask() {
        Task task = Task.builder().id(5L).title("Found").status(TaskStatus.IN_PROGRESS).build();
        when(taskRepo.findById(5L)).thenReturn(Optional.of(task));

        TaskResponse res = service.getById(5L);

        assertEquals("Found", res.title());
        assertEquals("IN_PROGRESS", res.status());
    }

    @Test
    void getById_throwsWhenMissing() {
        when(taskRepo.findById(404L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getById(404L));
    }

    @Test
    void delete_removesExistingTask() {
        Task task = Task.builder().id(7L).title("Del").status(TaskStatus.TODO).build();
        when(taskRepo.findById(7L)).thenReturn(Optional.of(task));

        service.delete(7L);

        verify(taskRepo).delete(task);
    }
}