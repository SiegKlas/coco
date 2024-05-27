package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deadline;

    private Integer pass;

    @Column(name = "L1")
    private Integer L1;

    @Column(name = "L2")
    private Integer L2;
}
