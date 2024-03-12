package ru.michael.coco.admin.deadlines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.level.Level;
import ru.michael.coco.level.LevelService;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.level_description.LevelDescriptionDTO;
import ru.michael.coco.level_description.LevelDescriptionMapper;
import ru.michael.coco.level_description.LevelDescriptionService;
import ru.michael.coco.topic.Topic;
import ru.michael.coco.topic.TopicService;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.topic_description.TopicDescriptionDTO;
import ru.michael.coco.topic_description.TopicDescriptionMapper;
import ru.michael.coco.topic_description.TopicDescriptionService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserDTO;
import ru.michael.coco.user.UserMapper;
import ru.michael.coco.user.UserService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin/deadlines")
public class AdminDeadlinesController {
    private final UserService userService;
    private final TopicDescriptionService topicDescriptionService;
    private final LevelDescriptionService levelDescriptionService;
    private final TopicService topicService;
    private final LevelService levelService;
    private final UserMapper userMapper;
    private final TopicDescriptionMapper topicDescriptionMapper;
    private final LevelDescriptionMapper levelDescriptionMapper;

    @Autowired
    public AdminDeadlinesController(UserService userService, TopicDescriptionService topicDescriptionService, LevelDescriptionService levelDescriptionService, TopicService topicService, LevelService levelService, UserMapper userMapper, TopicDescriptionMapper topicDescriptionMapper, LevelDescriptionMapper levelDescriptionMapper) {
        this.userService = userService;
        this.topicDescriptionService = topicDescriptionService;
        this.levelDescriptionService = levelDescriptionService;
        this.topicService = topicService;
        this.levelService = levelService;
        this.userMapper = userMapper;
        this.topicDescriptionMapper = topicDescriptionMapper;
        this.levelDescriptionMapper = levelDescriptionMapper;
    }

    @GetMapping
    public String deadlines() {
        return "deadlines";
    }

    @GetMapping("/global")
    public String globalDeadlines(Model model) {
        List<TopicDescription> topicDescriptionList = topicDescriptionService.findAllSorted();
        List<TopicDescriptionDTO> topicDescriptionDTOS = topicDescriptionList.stream().map(topicDescriptionMapper::toDTO).toList();
        model.addAttribute("topics", topicDescriptionDTOS);
        return "deadlines";
    }

    @GetMapping("/students")
    public String studentsDeadlines(Model model) {
        List<User> userList = userService.findAll();
        List<UserDTO> userDTOS = userList.stream().map(userMapper::toDTO).toList();
        List<TopicDescription> topicDescriptionList = topicDescriptionService.findAllSorted();
        List<TopicDescriptionDTO> topicDescriptionDTOS = topicDescriptionList.stream().map(topicDescriptionMapper::toDTO).toList();
        model.addAttribute("users", userDTOS);
        model.addAttribute("topics", topicDescriptionDTOS);
        return "deadlines";
    }

    @GetMapping("/levels")
    @ResponseBody
    public List<LevelDescriptionDTO> getLevelsForTopic(@RequestParam("topicNumber") Integer topicNumber) {
        List<LevelDescription> levels = levelDescriptionService.findAllByTopicDescriptionNumberSorted(topicNumber);
        return levels.stream().map(levelDescriptionMapper::toDTO).toList();
    }

    @PostMapping("/global")
    public ResponseEntity<String> processGlobalDeadlines(@RequestParam("topic") Integer topicNumber,
                                                         @RequestParam("level") Integer levelNumber,
                                                         @RequestParam("deadLine") Date deadLine,
                                                         @RequestParam("passTopic") Integer passTopic,
                                                         @RequestParam("passLevel") Integer passLevel) {
        try {
            LevelDescription levelDescription =
                    levelDescriptionService.findLevelDescriptionByTopicDescriptionNumberAndNumber(topicNumber, levelNumber).orElseThrow();
            levelDescription.setPass(passLevel);
            levelDescription.getTopicDescription().setPass(passTopic);
            levelDescription.getTopicDescription().setDeadLine(deadLine);
            levelDescriptionService.save(levelDescription);
            return ResponseEntity.ok("Data received successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("No such topic or level");
        }
    }

    @PostMapping("/students")
    public ResponseEntity<String> processStudentDeadlines(@RequestParam("topic") Integer topicNumber,
                                                          @RequestParam("level") Integer levelNumber,
                                                          @RequestParam("deadLine") Date deadLine,
                                                          @RequestParam("passTopic") Integer passTopic,
                                                          @RequestParam("passLevel") Integer passLevel,
                                                          @RequestParam("user") String userName) {
        try {
            User user = userService.findByUsername(userName).orElseThrow();
            LevelDescription levelDescription =
                    levelDescriptionService.findLevelDescriptionByTopicDescriptionNumberAndNumber(topicNumber, levelNumber).orElseThrow();
            TopicDescription topicDescription = levelDescription.getTopicDescription();
            Level level = levelService.findLevelByUserAndLevelDescription(user, levelDescription).orElseThrow();
            Topic topic = topicService.findTopicByUserAndTopicDescription(user, topicDescription).orElseThrow();
            level.setPass(passLevel);
            topic.setPass(passTopic);
            topic.setDeadLine(deadLine);
            levelService.save(level);
            topicService.save(topic);
            return ResponseEntity.ok("Data received successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("No such topic or level");
        }
    }
}
