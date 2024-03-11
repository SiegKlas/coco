package ru.michael.coco.level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.user.User;

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

    public Optional<Level> findLevelByUserAndLevelDescription(User user, LevelDescription levelDescription) {
        return levelRepository.findLevelByUserAndLevelDescription(user, levelDescription);
    }

    public void save(Level level) {
        levelRepository.save(level);
    }
}
