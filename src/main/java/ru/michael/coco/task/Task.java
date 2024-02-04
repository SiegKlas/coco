package ru.michael.coco.task;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.attempt.Attempt;
import ru.michael.coco.task_description.TaskDescription;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Task {
    @ManyToOne
    private final TaskDescription taskDescription;
    @OneToMany
    private final List<Attempt> attempt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer status = 0;
    private Boolean isLocked = true;
}
