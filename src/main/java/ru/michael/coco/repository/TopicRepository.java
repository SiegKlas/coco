package ru.michael.coco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}
