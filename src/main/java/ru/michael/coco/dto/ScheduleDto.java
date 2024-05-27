package ru.michael.coco.dto;

import lombok.Data;

@Data
public class ScheduleDto {
    private Long id;
    private String deadline;
    private Integer pass;
    private Integer L1;
    private Integer L2;
}
