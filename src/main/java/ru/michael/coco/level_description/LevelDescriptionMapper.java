package ru.michael.coco.level_description;

import org.mapstruct.Mapper;
import ru.michael.coco.task_description.TaskDescriptionMapper;

@Mapper(uses = TaskDescriptionMapper.class)
public interface LevelDescriptionMapper {
    LevelDescriptionDTO toDTO(LevelDescription levelDescription);
}
