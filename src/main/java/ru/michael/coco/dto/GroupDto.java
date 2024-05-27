package ru.michael.coco.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    private Long id;
    private String name;
    private Long teacherId;
    private List<Long> studentIds;
}
