package ru.michael.coco.task;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.user.UserEntity;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksByUser(UserEntity userEntity);

    Task findTaskByUserAndTaskDescription(UserEntity userEntity, TaskDescription taskDescription);
}
