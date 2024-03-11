package ru.michael.coco.admin.deadlines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.level_description.LevelDescriptionDTO;
import ru.michael.coco.level_description.LevelDescriptionMapper;
import ru.michael.coco.level_description.LevelDescriptionService;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.topic_description.TopicDescriptionDTO;
import ru.michael.coco.topic_description.TopicDescriptionMapper;
import ru.michael.coco.topic_description.TopicDescriptionService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    private Date parseDate(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
