package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.TopicSchedule;
import ru.michael.coco.entity.TopicScheduleId;

@Repository
public interface TopicScheduleRepository extends JpaRepository<TopicSchedule, TopicScheduleId> {
}
