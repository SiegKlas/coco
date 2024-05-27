package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.repository.TopicTaskRepository;

@Service
@Transactional
public class TopicTaskService {
    private final TopicTaskRepository topicTaskRepository;

    @Autowired
    public TopicTaskService(TopicTaskRepository topicTaskRepository) {
        this.topicTaskRepository = topicTaskRepository;
    }

    // Методы сервиса для TopicTaskRepository
}
