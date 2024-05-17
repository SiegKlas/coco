package ru.michael.coco.overseer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupDTO;
import ru.michael.coco.group.GroupMapper;
import ru.michael.coco.group.GroupService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserDTO;
import ru.michael.coco.user.UserMapper;
import ru.michael.coco.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/overseer/groups")
public class OverseerGroupController {
    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    @Autowired
    public OverseerGroupController(GroupService groupService, UserService userService, GroupMapper groupMapper, UserMapper userMapper) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String getGroupsPage(Model model) {
        List<Group> groups = groupService.findAllGroups();
        List<GroupDTO> groupDTOs = groups.stream().map(groupMapper::toDTO).collect(Collectors.toList());
        List<User> teachers = userService.findAllByRole(User.Role.TEACHER);
        List<UserDTO> teacherDTOs = teachers.stream().map(userMapper::toDTO).collect(Collectors.toList());
        List<User> students = userService.findAllByRole(User.Role.STUDENT);
        List<UserDTO> studentDTOs = students.stream().map(userMapper::toDTO).collect(Collectors.toList());
        model.addAttribute("groups", groupDTOs);
        model.addAttribute("teachers", teacherDTOs);
        model.addAttribute("students", studentDTOs);
        return "overseer/groups";
    }

    @PostMapping("/create")
    public String createGroup(@ModelAttribute GroupDTO groupDTO) {
        Group group = groupMapper.toEntity(groupDTO);
        group.setTeacher(userService.findById(groupDTO.getTeacherId()).orElseThrow(() -> new RuntimeException("Teacher not found")));
        group.setStudents(groupDTO.getStudentIds().stream().map(id -> userService.findById(id).orElseThrow(() -> new RuntimeException("Student not found"))).collect(Collectors.toList()));
        groupService.saveGroup(group);
        return "redirect:/overseer/groups";
    }

    @PostMapping("/update")
    public String updateGroup(@ModelAttribute GroupDTO groupDTO) {
        Group group = groupService.findById(groupDTO.getId()).orElseThrow(() -> new RuntimeException("Group not found"));
        group.setName(groupDTO.getName());
        group.setTeacher(userService.findById(groupDTO.getTeacherId()).orElseThrow(() -> new RuntimeException("Teacher not found")));
        group.setStudents(groupDTO.getStudentIds().stream().map(id -> userService.findById(id).orElseThrow(() -> new RuntimeException("Student not found"))).collect(Collectors.toList()));
        groupService.saveGroup(group);
        return "redirect:/overseer/groups";
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteGroup(@RequestParam Long groupId) {
        groupService.deleteGroupById(groupId);
        return ResponseEntity.ok("Group deleted successfully");
    }
}
