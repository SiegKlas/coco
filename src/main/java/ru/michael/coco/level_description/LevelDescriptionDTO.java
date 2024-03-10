package ru.michael.coco.level_description;

import lombok.Data;
import ru.michael.coco.task_description.TaskDescriptionDTO;

import java.util.Set;

@Data
public class LevelDescriptionDTO {
    private final Integer number;
    private final Set<TaskDescriptionDTO> taskDescriptionDTOSet;
}
