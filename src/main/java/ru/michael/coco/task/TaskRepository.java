package ru.michael.coco.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByTaskDescriptionNumber(Integer number);

    Optional<Task> findTaskByUserAndTaskDescription(User user, TaskDescription taskDescription);

    List<Task> findTasksByUser(User user);

    List<Task> findTasksByUserAndTaskDescription_LevelDescription_TopicDescriptionAndTaskDescription_LevelDescriptionOrderByTaskDescription_Number(
            User user, TopicDescription topicDescription, LevelDescription levelDescription
    );
}
