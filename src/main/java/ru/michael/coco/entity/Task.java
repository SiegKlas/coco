package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level_number")
    private Integer levelNumber;

    @Column(name = "task_number")
    private Integer taskNumber;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "task_path")
    private String taskPath;
}
