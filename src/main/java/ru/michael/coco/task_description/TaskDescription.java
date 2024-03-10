package ru.michael.coco.task_description;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.level_description.LevelDescription;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TaskDescription {
    private final String fileName;
    private final Integer number;
    private final String name;
    private final String contextPath;
    @ManyToOne
    private final LevelDescription levelDescription;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
