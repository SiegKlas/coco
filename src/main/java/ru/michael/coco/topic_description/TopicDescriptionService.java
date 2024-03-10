package ru.michael.coco.topic_description;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void save(TopicDescription topicDescription) {
        topicDescriptionRepository.save(topicDescription);
    }

    public List<TopicDescription> findAllSorted() {
        return topicDescriptionRepository.findAllByOrderByNumberAsc();
    }
}
