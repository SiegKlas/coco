package ru.michael.coco.topic_description;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopicDescriptionService {
    private final TopicDescriptionRepository topicDescriptionRepository;

    @Autowired
    public TopicDescriptionService(TopicDescriptionRepository topicDescriptionRepository) {
        this.topicDescriptionRepository = topicDescriptionRepository;
    }

    public Optional<TopicDescription> findTopicDescriptionByNumber(Integer number) {
        return topicDescriptionRepository.findTopicDescriptionByNumber(number);
    }

    public void deleteAll() {
        topicDescriptionRepository.deleteAll();
    }
}
