package ru.michael.coco.dto;


import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private Integer levelNumber;
    private Integer taskNumber;
    private String taskName;
    private String taskPath;
}
