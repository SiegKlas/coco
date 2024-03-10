package ru.michael.coco.topic_description;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import ru.michael.coco.level_description.LevelDescription;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TopicDescription {
    private final Integer number;
    private final String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<LevelDescription> levelDescriptions;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private Date deadLine = null;
}
