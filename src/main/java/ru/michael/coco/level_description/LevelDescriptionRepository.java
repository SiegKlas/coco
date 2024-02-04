package ru.michael.coco.level_description;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LevelDescriptionRepository extends JpaRepository<LevelDescription, Long> {
    Optional<LevelDescription> findLevelDescriptionByNumber(Integer number);
}
