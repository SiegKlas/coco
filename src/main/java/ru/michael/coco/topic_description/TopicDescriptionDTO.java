package ru.michael.coco.topic_description;

import lombok.Data;
import ru.michael.coco.level_description.LevelDescriptionDTO;

import java.util.Set;

@Data
public class TopicDescriptionDTO {
    private final Integer number;
    private final String name;
    private final Set<LevelDescriptionDTO> levelDescriptionDTOSet;
}
