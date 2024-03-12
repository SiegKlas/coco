package ru.michael.coco.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Optional<Topic> findTopicByTopicDescriptionNumber(Integer number) {
        return topicRepository.findTopicByTopicDescriptionNumber(number);
    }

    public Optional<Topic> findTopicByUserAndTopicDescription(User user, TopicDescription topicDescription) {
        return topicRepository.findTopicByUserAndTopicDescription(user, topicDescription);
    }

    public void save(Topic topic) {
        topicRepository.save(topic);
    }

    public List<Topic> findAllByUser(User user) {
        return topicRepository.findAllByUserOrderByTopicDescription_Number(user);
    }
}
