package ru.michael.coco.attempt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.task.Task;

import java.util.List;

@Service
public class AttemptService {
    private final AttemptRepository attemptRepository;

    @Autowired
    public AttemptService(AttemptRepository attemptRepository) {
        this.attemptRepository = attemptRepository;
    }

    public void save(Attempt attempt) {
        attemptRepository.save(attempt);
    }

    public List<Attempt> findAttemptsByTaskAndResponseStatus(Task task, String status) {
        return attemptRepository.findAttemptsByTaskAndResponse_Status(task, status);
    }
}
