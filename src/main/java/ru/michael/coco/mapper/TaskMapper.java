package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.TaskDto;
import ru.michael.coco.entity.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toDto(Task task);

    Task toEntity(TaskDto taskDto);
}
