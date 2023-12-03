package ru.michael.coco.admin.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.michael.coco.level.Level;
import ru.michael.coco.level.LevelService;
import ru.michael.coco.task.Task;
import ru.michael.coco.task.TaskService;
import ru.michael.coco.topic.Topic;
import ru.michael.coco.topic.TopicService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class CsvWriter {
    public static final String CSV_FILE_PATH = "D:\\diploma\\coco\\stats\\file.csv";
    private final UserService userService;
    private final TopicService topicService;
    private final LevelService levelService;
    private final TaskService taskService;

    @Autowired
    public CsvWriter(UserService userService, TopicService topicService, LevelService levelService, TaskService taskService) {
        this.userService = userService;
        this.topicService = topicService;
        this.levelService = levelService;
        this.taskService = taskService;
    }

    public static void writeMapToCsv(Map<String, Map<String, Map<String, Integer>>> data) {
        try (FileWriter writer = new FileWriter(CSV_FILE_PATH)) {
            writer.append("Student\tTopic\tLevel\tScore\n");

            for (Map.Entry<String, Map<String, Map<String, Integer>>> studentEntry : data.entrySet()) {
                String studentName = studentEntry.getKey();
                Map<String, Map<String, Integer>> topicData = studentEntry.getValue();

                for (Map.Entry<String, Map<String, Integer>> topicEntry : topicData.entrySet()) {
                    String topicName = topicEntry.getKey();
                    Map<String, Integer> levelData = topicEntry.getValue();

                    Map<String, Integer> sortedLevelData = new TreeMap<>(levelData);

                    for (Map.Entry<String, Integer> levelEntry : sortedLevelData.entrySet()) {
                        String levelName = levelEntry.getKey();
                        int score = levelEntry.getValue();
                        writer.append(String.join("\t", studentName, topicName, levelName, String.valueOf(score), "\n"));
                    }
                }
            }

            System.out.println("CSV файл успешно создан: " + CSV_FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Map<String, Map<String, Integer>>> collectStats() {
        Map<String, Map<String, Map<String, Integer>>> stats = new HashMap<>();
        List<User> users = userService.findAll();
        for (var user : users) {
            Map<String, Map<String, Integer>> userData = new HashMap<>();
            List<Topic> topics = topicService.findAllByUser(user);
            for (var topic : topics) {
                Map<String, Integer> topicData = new HashMap<>();
                List<Level> levels = levelService.findAllByUserAndTopicDescriptionNumber(user, topic.getTopicDescription());
                for (var level : levels) {
                    List<Task> tasks = taskService.findTasksByUserAndTopicDescriptionAndLevelDescription(
                            user, topic.getTopicDescription(), level.getLevelDescription()
                    );
                    long sum = tasks.stream().map(taskService::getStatus).filter(status -> status.equals(Task.STATUS.SUCCESS)).count();
                    topicData.put("Level " + level.getLevelDescription().getNumber().toString(), (int) sum);
                }
                userData.put(topic.getTopicDescription().getName(), topicData);
            }
            stats.put(user.getUsername(), userData);
        }
        return stats;
    }
}
