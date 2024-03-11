package ru.michael.coco.admin.deadlines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.level_description.LevelDescriptionDTO;
import ru.michael.coco.level_description.LevelDescriptionMapper;
import ru.michael.coco.level_description.LevelDescriptionService;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.topic_description.TopicDescriptionDTO;
import ru.michael.coco.topic_description.TopicDescriptionMapper;
import ru.michael.coco.topic_description.TopicDescriptionService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/admin/deadlines")
public class AdminDeadlinesController {
    private final TopicDescriptionService topicDescriptionService;
    private final LevelDescriptionService levelDescriptionService;
    private final TopicDescriptionMapper topicDescriptionMapper;
    private final LevelDescriptionMapper levelDescriptionMapper;

    @Autowired
    public AdminDeadlinesController(TopicDescriptionService topicDescriptionService, LevelDescriptionService levelDescriptionService, TopicDescriptionMapper topicDescriptionMapper, LevelDescriptionMapper levelDescriptionMapper) {
        this.topicDescriptionService = topicDescriptionService;
        this.levelDescriptionService = levelDescriptionService;
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

    @GetMapping("/levels")
    @ResponseBody
    public List<LevelDescriptionDTO> getLevelsForTopic(@RequestParam("topicNumber") Integer topicNumber) {
        List<LevelDescription> levels = levelDescriptionService.findAllByTopicDescriptionNumberSorted(topicNumber);
        return levels.stream().map(levelDescriptionMapper::toDTO).toList();
    }

    @PostMapping("/global")
    public ResponseEntity<String> processDeadlines(@RequestParam("topic") Integer topicNumber,
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
            return ResponseEntity.ok("Data received successfully!");
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body("No such topic or level");
        }
    }
}
