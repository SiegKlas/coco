package ru.michael.coco.dto;

import lombok.Data;

@Data
public class JournalDto {
    private Long userId;
    private Long taskId;
    private Long solutionId;
    private String date;
}
