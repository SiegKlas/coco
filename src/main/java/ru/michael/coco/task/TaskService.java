package ru.michael.coco.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.user.UserRepository;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
    }

    public Optional<Task> findTaskByTaskDescriptionNumber(Integer number) {
        return taskRepository.findTaskByTaskDescriptionNumber(number);
    }

    public void save(Task task) {
        taskRepository.save(task);
    }
}
