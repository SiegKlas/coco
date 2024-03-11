package ru.michael.coco.admin.deadlines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.level_description.LevelDescriptionService;
import ru.michael.coco.topic_description.TopicDescription;
import ru.michael.coco.topic_description.TopicDescriptionService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/admin/deadlines")
public class AdminDeadlinesController {
    private final TopicDescriptionService topicDescriptionService;
    private final LevelDescriptionService levelDescriptionService;

    @Autowired
    public AdminDeadlinesController(TopicDescriptionService topicDescriptionService, LevelDescriptionService levelDescriptionService) {
        this.topicDescriptionService = topicDescriptionService;
        this.levelDescriptionService = levelDescriptionService;
    }

    @GetMapping("/topic/{topicNumber}")
    public String showTopicDeadlineForm(@PathVariable Integer topicNumber, Model model) {
        TopicDescription topicDescription = topicDescriptionService.findTopicDescriptionByNumber(topicNumber).orElseThrow();
        model.addAttribute("topic", topicDescription);
        return "topic_deadline_form";
    }

    @PostMapping("/topic/{topicNumber}")
    public String updateTopicDeadline(@PathVariable Integer topicNumber, @RequestParam("deadline") String deadline, Model model) {
        TopicDescription topicDescription = topicDescriptionService.findTopicDescriptionByNumber(topicNumber).orElseThrow();
        topicDescription.setDeadLine(parseDate(deadline));
        topicDescriptionService.save(topicDescription);
        return "redirect:/";
    }

    @GetMapping("/pass/level/{levelNumber}/topic/{topicNumber}")
    public String showLevelPassForm(@PathVariable Integer levelNumber, @PathVariable Integer topicNumber, Model model) {
        TopicDescription topicDescription = topicDescriptionService.findTopicDescriptionByNumber(topicNumber).orElseThrow();
        LevelDescription levelDescription = topicDescription.getLevelDescriptions().stream()
                .filter(level -> level.getNumber().equals(levelNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Level not found"));
        model.addAttribute("topic", topicDescription);
        model.addAttribute("level", levelDescription);
        return "level_pass_form";
    }

    @PostMapping("/pass/level/{levelNumber}/topic/{topicNumber}")
    public String updateLevelPass(@PathVariable Integer levelNumber, @PathVariable Integer topicNumber, @RequestParam("pass") Integer pass, Model model) {
        TopicDescription topicDescription = topicDescriptionService.findTopicDescriptionByNumber(topicNumber).orElseThrow();
        LevelDescription levelDescription = topicDescription.getLevelDescriptions().stream()
                .filter(level -> level.getNumber().equals(levelNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Level not found"));
        levelDescription.setPass(pass);
        levelDescriptionService.save(levelDescription);
        return "redirect:/";
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
