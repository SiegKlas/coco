package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Topic;
import ru.michael.coco.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TopicService {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic createTopic(Topic topic) {
        // Логика, если необходимо, перед сохранением темы
        return topicRepository.save(topic);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }

    public Topic updateTopic(Topic topic) {
        // Логика обновления темы, если необходимо
        return topicRepository.save(topic);
    }

    public void deleteTopicById(Long id) {
        topicRepository.deleteById(id);
    }
}

