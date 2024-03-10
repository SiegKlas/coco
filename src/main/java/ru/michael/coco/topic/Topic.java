package ru.michael.coco.topic;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.user.UserEntity;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Topic {
    @ManyToOne
    private final UserEntity user;
    @ManyToOne
    private final TopicDescription topicDescription;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
