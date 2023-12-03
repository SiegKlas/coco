package ru.michael.coco.level_description;

import lombok.Data;
import ru.michael.coco.task_description.TaskDescriptionDTO;

import java.util.Set;

@Data
public class LevelDescriptionDTO {
    private Integer number;
    private Set<TaskDescriptionDTO> taskDescriptionDTOSet;
}
