package ru.michael.coco.group;

import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {
    private Long id;
    private String name;
    private Long teacherId;
    private List<Long> studentIds;
}
