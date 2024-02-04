package ru.michael.coco.task_description;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskDescriptionRepository extends JpaRepository<TaskDescription, Long> {
    Optional<TaskDescription> findTaskDescriptionByNumber(Integer number);
}
