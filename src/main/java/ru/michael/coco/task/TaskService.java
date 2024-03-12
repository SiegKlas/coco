package ru.michael.coco.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.attempt.AttemptService;
import ru.michael.coco.level.Level;
import ru.michael.coco.level.LevelService;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.task_description.TaskDescription;
import ru.michael.coco.topic.Topic;
import ru.michael.coco.topic.TopicService;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.user.User;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final LevelService levelService;
    private final TopicService topicService;
    private final AttemptService attemptService;

    @Autowired
    public TaskService(TaskRepository taskRepository, LevelService levelService,
                       TopicService topicService, AttemptService attemptService) {
        this.taskRepository = taskRepository;
        this.levelService = levelService;
        this.topicService = topicService;
        this.attemptService = attemptService;
    }

    public Optional<Task> findTaskByTaskDescriptionNumber(Integer number) {
        return taskRepository.findTaskByTaskDescriptionNumber(number);
    }

    public Optional<Task> findTaskByUserAndTaskDescription(User user, TaskDescription taskDescription) {
        return taskRepository.findTaskByUserAndTaskDescription(user, taskDescription);
    }

    //TODO: transfer logic of level and topic save
    public void save(Task task) {
        taskRepository.save(task);
        if (levelService.findLevelByUserAndLevelDescription(task.getUser(), task.getTaskDescription().getLevelDescription()).isEmpty()) {
            levelService.save(new Level(task.getUser(), task.getTaskDescription().getLevelDescription()));
        }
        if (topicService.findTopicByUserAndTopicDescription(task.getUser(), task.getTaskDescription().getLevelDescription().getTopicDescription()).isEmpty()) {
            topicService.save(new Topic(task.getUser(), task.getTaskDescription().getLevelDescription().getTopicDescription()));
        }
    }

    public List<Task> findTasksByUser(User user) {
        return taskRepository.findTasksByUser(user);
    }

    public List<Task> findTasksByUserAndTopicDescriptionAndLevelDescription(
            User user, TopicDescription topicDescription, LevelDescription levelDescription
    ) {
        return taskRepository.findTasksByUserAndTaskDescription_LevelDescription_TopicDescriptionAndTaskDescription_LevelDescriptionOrderByTaskDescription_Number(
                user, topicDescription, levelDescription
        );
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
