package ru.michael.coco.task_description;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskDescriptionMapper {
    TaskDescriptionDTO toDTO(TaskDescription taskDescription);
}
