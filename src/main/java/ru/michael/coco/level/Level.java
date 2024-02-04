package ru.michael.coco.level;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.task.Task;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Level {
    @OneToMany
    private final List<Task> tasks;
    @ManyToOne
    private final LevelDescription levelDescription;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isLocked = true;
}
