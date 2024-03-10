package ru.michael.coco.topic;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.user.User;

import java.util.Date;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Topic {
    @ManyToOne
    private final User user;
    @ManyToOne
    private final TopicDescription topicDescription;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private Date deadLine = null;
}
