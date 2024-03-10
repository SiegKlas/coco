package ru.michael.coco.task;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByTaskDescriptionNumber(Integer number);

    Optional<Task> findTaskByUserAndTaskDescription(User user, TaskDescription taskDescription);

    List<Task> findTasksByUser(User user);
}
