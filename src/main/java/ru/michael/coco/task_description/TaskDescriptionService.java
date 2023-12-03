package ru.michael.coco.task_description;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.level_description.LevelDescriptionService;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.topic_description.TopicDescriptionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TaskDescriptionService {
    private final LevelDescriptionService levelDescriptionService;
    private final TopicDescriptionService topicDescriptionService;
    private final TaskDescriptionRepository taskDescriptionRepository;

    @Autowired
    public TaskDescriptionService(LevelDescriptionService levelDescriptionService, TopicDescriptionService topicDescriptionService, TaskDescriptionRepository taskDescriptionRepository) {
        this.levelDescriptionService = levelDescriptionService;
        this.topicDescriptionService = topicDescriptionService;
        this.taskDescriptionRepository = taskDescriptionRepository;
    }

    public Optional<TaskDescription> findTaskDescriptionByNumber(Integer number) {
        return taskDescriptionRepository.findTaskDescriptionByNumber(number);
    }

    private String fileNameBy(Path path) {
        return List.of(path.toString().split("\\\\")).getLast();
    }

    public Map<String, String> taskInfo(Path path) throws IOException {
        final String fileName = fileNameBy(path);

        Pattern pattern = Pattern.compile("NSU-T(\\d{2})L(\\d)e(\\d{2})");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            String topicNumber = matcher.group(1);
            String levelNumber = matcher.group(2);
            String taskNumber = matcher.group(3);

            ObjectMapper objectMapper = new ObjectMapper();
            Path metaPath = path.resolve("meta.json");

            MetaData metaData = objectMapper.readValue(Files.newBufferedReader(metaPath), MetaData.class);
            String topicName = metaData.getTopic();
            String taskName = metaData.getTask();

            return Map.of(
                    "topicNumber", topicNumber, "levelNumber", levelNumber, "taskNumber", taskNumber,
                    "topicName", topicName, "taskName", taskName,
                    "fileName", fileName, "path", path.toString()
            );
        } else {
            throw new RuntimeException("Task description corrupted");
        }
    }

    @Transactional
    public void createTaskDescriptionByPath(Path path) throws IOException {
        var taskInfo = taskInfo(path);
        Optional<TopicDescription> topicDescriptionOptional =
                topicDescriptionService.findTopicDescriptionByNumber(Integer.valueOf(taskInfo.get("topicNumber")));
        TopicDescription topicDescription = topicDescriptionOptional.orElse(
                new TopicDescription(
                        Integer.valueOf(taskInfo.get("topicNumber")),
                        taskInfo.get("topicName"),
                        new HashSet<>()
                )
        );
        Optional<LevelDescription> levelDescriptionOptional =
                levelDescriptionService.findLevelDescriptionByTopicDescriptionNumberAndNumber(
                        Integer.valueOf(taskInfo.get("topicNumber")),
                        Integer.valueOf(taskInfo.get("levelNumber"))
                );
        LevelDescription levelDescription = levelDescriptionOptional.orElse(
                new LevelDescription(
                        Integer.valueOf(taskInfo.get("levelNumber")),
                        topicDescription,
                        new HashSet<>()
                )
        );
        topicDescription.getLevelDescriptions().add(levelDescription);
        Optional<TaskDescription> taskDescriptionOptional =
                findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(
                        Integer.valueOf(taskInfo.get("topicNumber")),
                        Integer.valueOf(taskInfo.get("levelNumber")),
                        Integer.valueOf(taskInfo.get("taskNumber"))
                );
        TaskDescription taskDescription = taskDescriptionOptional.orElse(
                new TaskDescription(
                        taskInfo.get("fileName"),
                        Integer.valueOf(taskInfo.get("taskNumber")),
                        taskInfo.get("taskName"),
                        taskInfo.get("path"),
                        levelDescription
                )
        );
        levelDescription.getTaskDescriptions().add(taskDescription);
        topicDescriptionService.save(topicDescription);
    }

    public Optional<TaskDescription> findTaskDescriptionByTopicNumberAndLevelNumberAndTaskNumber(
            Integer topicNumber,
            Integer levelNumber,
            Integer taskNumber
    ) {
        return taskDescriptionRepository.findTaskDescriptionByLevelDescription_TopicDescription_NumberAndLevelDescription_NumberAndNumber(
                topicNumber,
                levelNumber,
                taskNumber
        );
    }

    public List<TaskDescription> findAll() {
        return taskDescriptionRepository.findAll();
    }

    public List<TaskDescription> findTaskDescriptionsByTopicNumberAndLevelNumberSorted(
            Integer topicNumber,
            Integer levelNumber
    ) {
        return taskDescriptionRepository.findTaskDescriptionsByLevelDescription_TopicDescription_NumberAndLevelDescription_NumberOrderByNumber(
                topicNumber,
                levelNumber
        );
    }

    @Data
    private static class MetaData {
        private String topic;
        private String task;
    }
}
