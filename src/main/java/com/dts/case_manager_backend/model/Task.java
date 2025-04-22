package com.dts.case_manager_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "tasks")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String status;

    @Column (name = "created_date")
    private LocalDateTime createdDate;

    @Column (name = "due_date")
    private LocalDateTime dueDate;
}
