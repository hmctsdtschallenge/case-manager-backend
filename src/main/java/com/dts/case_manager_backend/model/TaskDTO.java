package com.dts.case_manager_backend.model;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public record TaskDTO(
        String title,
        String description,
        String status,
        LocalDateTime createdDate,
        LocalDateTime dueDate) {
}
