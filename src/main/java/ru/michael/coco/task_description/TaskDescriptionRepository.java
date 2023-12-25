package ru.michael.coco.task_description;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskDescriptionRepository extends JpaRepository<TaskDescription, Long> {
    @Query("SELECT DISTINCT td.level FROM TaskDescription td WHERE td.topic = :topic")
    List<Integer> getLevelsForTopic(@Param("topic") Integer topic);

    @Query("SELECT DISTINCT td.number FROM TaskDescription td WHERE td.topic = :topic AND td.level = :level")
    List<Integer> getNumbersForTopicAndLevel(@Param("topic") Integer topic, @Param("level") Integer level);

    @Query("SELECT td FROM TaskDescription td WHERE td.topic = :topic AND td.level = :level AND td.number = :number")
    Optional<TaskDescription> getTaskDescription(@Param("topic") Integer topic, @Param("level") Integer level,
                                                 @Param("number") Integer number);

    TaskDescription findTaskDescriptionByTopicAndLevelAndNumber(Integer topic, Integer level, Integer number);
}
