package ru.michael.coco.attempt;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.michael.coco.task.Task;
import ru.michael.coco.user.UserEntity;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Attempt {
    @ManyToOne
    private final UserEntity user;
    @ManyToOne
    private final Task task;
    private final String solutionPath;
    @OneToOne
    private final Response response;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
