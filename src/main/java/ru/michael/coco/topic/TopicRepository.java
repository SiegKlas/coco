package ru.michael.coco.topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findTopicByTopicDescriptionNumber(Integer number);

    Optional<Topic> findTopicByUserAndTopicDescription(User user, TopicDescription topicDescription);

    List<Topic> findAllByUserOrderByTopicDescription_Number(User user);
}
