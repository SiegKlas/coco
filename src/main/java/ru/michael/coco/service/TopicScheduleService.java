package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.repository.TopicScheduleRepository;

@Service
@Transactional
public class TopicScheduleService {
    private final TopicScheduleRepository topicScheduleRepository;

    @Autowired
    public TopicScheduleService(TopicScheduleRepository topicScheduleRepository) {
        this.topicScheduleRepository = topicScheduleRepository;
    }

    // Методы сервиса для TopicScheduleRepository
}
