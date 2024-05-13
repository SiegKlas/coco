package ru.michael.coco.overseer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/overseer")
public class OverseerController {
    private final UserService userService;
    private final GroupService groupService;

    public OverseerController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping
    public String getOverseerMainPage() {
        return "overseer/main";
    }

    @GetMapping("/distribution")
    public String getDistributionPage(Model model) {
        model.addAttribute("teachers", userService.findAllByRole(User.Role.TEACHER));
        model.addAttribute("students", userService.findAllByRole(User.Role.STUDENT));
        model.addAttribute("groups", groupService.findAllGroups());
        return "overseer/distribution";
    }

    @PostMapping("/createGroup")
    public String createGroup(@RequestParam String groupName, @RequestParam Long teacherId, @RequestParam List<Long> studentIds) {
        Group group = new Group();
        group.setName(groupName);
        group.setTeacher(userService.findById(teacherId).orElseThrow());
        group.setStudents(studentIds.stream().map(id -> userService.findById(id).orElseThrow()).collect(Collectors.toList()));
        groupService.saveGroup(group);
        return "redirect:/overseer/distribution";
    }

    @GetMapping("/editGroup")
    public String editGroupPage(@RequestParam Long groupId, Model model) {
        Group group = groupService.findById(groupId).orElseThrow();
        model.addAttribute("group", group);
        model.addAttribute("allTeachers", userService.findAllByRole(User.Role.TEACHER));
        model.addAttribute("allStudents", userService.findAllByRole(User.Role.STUDENT));
        return "overseer/editGroup";
    }

    @PostMapping("/updateGroup")
    public String updateGroup(@RequestParam Long groupId, @RequestParam String groupName, @RequestParam Long teacherId, @RequestParam List<Long> studentIds) {
        Group group = groupService.findById(groupId).orElseThrow();
        group.setName(groupName);
        group.setTeacher(userService.findById(teacherId).orElseThrow());
        group.setStudents(studentIds.stream().map(id -> userService.findById(id).orElseThrow()).collect(Collectors.toList()));
        groupService.saveGroup(group);
        return "redirect:/overseer/distribution";
    }
}
