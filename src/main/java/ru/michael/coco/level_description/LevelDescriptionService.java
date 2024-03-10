package ru.michael.coco.level_description;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LevelDescriptionService {
    private final LevelDescriptionRepository levelDescriptionRepository;

    @Autowired
    public LevelDescriptionService(LevelDescriptionRepository levelDescriptionRepository) {
        this.levelDescriptionRepository = levelDescriptionRepository;
    }

    public Optional<LevelDescription> findLevelDescriptionByNumber(Integer number) {
        return levelDescriptionRepository.findLevelDescriptionByNumber(number);
    }

    public Optional<LevelDescription> findLevelDescriptionByTopicDescriptionNumberAndNumber(Integer topicDescription_Number, Integer number) {
        return levelDescriptionRepository.findLevelDescriptionByTopicDescription_NumberAndNumber(topicDescription_Number, number);
    }

    public List<LevelDescription> findAllByTopicDescriptionNumberSorted(Integer topicDescriptionNumber) {
        return levelDescriptionRepository.findAllByTopicDescription_NumberOrderByNumberAsc(topicDescriptionNumber);
    }
}
