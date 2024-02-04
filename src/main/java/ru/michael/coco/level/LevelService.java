package ru.michael.coco.level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LevelService {
    private final LevelRepository levelRepository;

    @Autowired
    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public Optional<Level> findLevelByLevelDescriptionNumber(Integer number) {
        return levelRepository.findLevelByLevelDescriptionNumber(number);
    }
}
