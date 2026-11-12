package com.example.taskflow.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
        @NotBlank(message = "Name is required") String name,
        String description,
        Long ownerId
) {}