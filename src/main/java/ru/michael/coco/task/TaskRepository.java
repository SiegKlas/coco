package ru.michael.coco.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findTaskByTaskDescriptionNumber(Integer number);
}
