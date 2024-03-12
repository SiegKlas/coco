package ru.michael.coco.level;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    Optional<Level> findLevelByLevelDescriptionNumber(Integer number);

    Optional<Level> findLevelByUserAndLevelDescription(User user, LevelDescription levelDescription);

    List<Level> findAllByUserAndLevelDescription_TopicDescriptionOrderByLevelDescription_Number(User user, TopicDescription topicDescription);
}
