package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer status;

    private String output;

    private String error;

    @Column(name = "solution_path")
    private String solutionPath;
}
