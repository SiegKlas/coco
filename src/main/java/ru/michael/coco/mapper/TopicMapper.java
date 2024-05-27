package ru.michael.coco.mapper;

import org.mapstruct.Mapper;
import ru.michael.coco.dto.TopicDto;
import ru.michael.coco.entity.Topic;

@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicDto toDto(Topic topic);

    Topic toEntity(TopicDto topicDto);
}
