package ru.michael.coco.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    Optional<Topic> findTopicByTopicDescriptionNumber(Integer number) {
        return topicRepository.findTopicByTopicDescriptionNumber(number);
    }
}
