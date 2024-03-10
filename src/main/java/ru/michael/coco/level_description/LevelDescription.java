package ru.michael.coco.level_description;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.topic_description.TopicDescription;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class LevelDescription {
    private final Integer number;
    @ManyToOne
    private final TopicDescription topicDescription;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<TaskDescription> taskDescriptions;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private Date deadLine = null;
}
