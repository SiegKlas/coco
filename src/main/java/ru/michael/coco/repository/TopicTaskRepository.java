package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.TopicTask;
import ru.michael.coco.entity.TopicTaskId;

@Repository
public interface TopicTaskRepository extends JpaRepository<TopicTask, TopicTaskId> {
}