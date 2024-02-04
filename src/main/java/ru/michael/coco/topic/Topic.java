package ru.michael.coco.topic;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.level.Level;
import ru.michael.coco.topic_description.TopicDescription;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Topic {
    @OneToMany
    private final List<Level> levels;
    @ManyToOne
    private final TopicDescription topicDescription;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
