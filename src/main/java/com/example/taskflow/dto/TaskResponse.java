package com.example.taskflow.dto;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        String status,
        LocalDate deadline,
        Long projectId,
        Long assigneeId
) {}
