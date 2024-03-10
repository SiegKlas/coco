package ru.michael.coco.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.attempt.AttemptService;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AttemptService attemptService;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, AttemptService attemptService) {
        this.taskRepository = taskRepository;
        this.attemptService = attemptService;
    }

    public Optional<Task> findTaskByTaskDescriptionNumber(Integer number) {
        return taskRepository.findTaskByTaskDescriptionNumber(number);
    }

    public Optional<Task> findTaskByUserAndTaskDescription(User user, TaskDescription taskDescription) {
        return taskRepository.findTaskByUserAndTaskDescription(user, taskDescription);
    }

    public void save(Task task) {
        taskRepository.save(task);
    }

    public List<Task> findTasksByUser(User user) {
        return taskRepository.findTasksByUser(user);
    }

    public Task.STATUS getStatus(Task task) {
        if (!attemptService.findAttemptsByTaskAndResponseStatus(task, "success").isEmpty()) {
            return Task.STATUS.SUCCESS;
        }
        if (task.getAttempts().isEmpty()) {
            return Task.STATUS.UNSOLVED;
        }
        return Task.STATUS.FAIL;
    }
}
