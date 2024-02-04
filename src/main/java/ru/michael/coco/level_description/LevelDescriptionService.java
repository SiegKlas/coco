package ru.michael.coco.level_description;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
