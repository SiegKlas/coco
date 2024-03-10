package ru.michael.coco.attempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.michael.coco.task.Task;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    List<Attempt> findAttemptsByTaskAndResponse_Status(Task task, String status);
}
