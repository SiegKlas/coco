package ru.michael.coco.dto;

import lombok.Data;

@Data
public class SolutionDto {
    private Long id;
    private Integer status;
    private String output;
    private String error;
    private String solutionPath;
}