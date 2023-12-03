package ru.michael.coco.task_description;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskDescriptionRepository extends JpaRepository<TaskDescription, Long> {
    Optional<TaskDescription> findTaskDescriptionByNumber(Integer number);

    Optional<TaskDescription> findTaskDescriptionByLevelDescription_TopicDescription_NumberAndLevelDescription_NumberAndNumber(
            Integer LevelDescription_TopicDescription_Number,
            Integer LevelDescription_Number,
            Integer Number
    );

    List<TaskDescription> findTaskDescriptionsByLevelDescription_TopicDescription_NumberAndLevelDescription_NumberOrderByNumber(
            Integer LevelDescription_TopicDescription_Number,
            Integer LevelDescription_Number
    );
}
