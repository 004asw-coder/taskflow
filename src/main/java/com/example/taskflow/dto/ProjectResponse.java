package com.example.taskflow.dto;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        Long ownerId
) {}
