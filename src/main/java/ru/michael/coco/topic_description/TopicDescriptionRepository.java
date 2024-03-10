package ru.michael.coco.topic_description;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicDescriptionRepository extends JpaRepository<TopicDescription, Long> {
    Optional<TopicDescription> findTopicDescriptionByNumber(Integer number);

    List<TopicDescription> findAllByOrderByNumberAsc();
}
