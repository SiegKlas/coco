package ru.michael.coco.topic_description;

import org.mapstruct.Mapper;
import ru.michael.coco.level_description.LevelDescriptionMapper;

@Mapper(componentModel = "spring", uses = LevelDescriptionMapper.class)
public interface TopicDescriptionMapper {
    TopicDescriptionDTO toDTO(TopicDescription topicDescription);
}
