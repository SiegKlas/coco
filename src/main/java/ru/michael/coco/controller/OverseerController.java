package ru.michael.coco.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.dto.GroupDto;
import ru.michael.coco.dto.UserDto;
import ru.michael.coco.entity.Group;
import ru.michael.coco.entity.User;
import ru.michael.coco.mapper.GroupMapper;
import ru.michael.coco.mapper.UserMapper;
import ru.michael.coco.service.GroupService;
import ru.michael.coco.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/overseer")
public class OverseerController {
    private final GroupService groupService;
    private final UserService userService;
    private final GroupMapper groupMapper;
    private final UserMapper userMapper;

    @Autowired
    public OverseerController(GroupService groupService, UserService userService, GroupMapper groupMapper, UserMapper userMapper) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public String dashboard() {
        return "overseer/dashboard";
    }

    @GetMapping("/groups")
    public String manageGroups(Model model) {
        List<GroupDto> groups = groupService.getAllGroups().stream().map(groupMapper::toDto).collect(Collectors.toList());
        List<UserDto> teachers = userService.getAllTeachers().stream().map(userMapper::toDto).collect(Collectors.toList());
        List<UserDto> students = userService.getAllStudents().stream().map(userMapper::toDto).collect(Collectors.toList());
        model.addAttribute("groups", groups);
        model.addAttribute("teachers", teachers);
        model.addAttribute("students", students);
        return "overseer/groups";
    }

    @PostMapping("/groups/create")
    public String createGroup(@ModelAttribute GroupDto groupDto) {
        Group group = groupMapper.toEntity(groupDto);
        group = groupService.createGroup(group);
        groupService.assignUsersToGroup(group, groupDto.getTeacherId(), groupDto.getStudentIds());
        return "redirect:/overseer/groups";
    }

    @PostMapping("/groups/update")
    public String updateGroup(@ModelAttribute GroupDto groupDto) {
        Group group = groupMapper.toEntity(groupDto);
        group = groupService.updateGroup(group);
        groupService.assignUsersToGroup(group, groupDto.getTeacherId(), groupDto.getStudentIds());
        return "redirect:/overseer/groups";
    }

    @PostMapping("/groups/delete")
    public String deleteGroup(@RequestParam Long groupId) {
        groupService.deleteGroupById(groupId);
        return "redirect:/overseer/groups";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<UserDto> users = userService.getAllUsers().stream().map(userMapper::toDto).collect(Collectors.toList());
        List<GroupDto> groups = groupService.getAllGroups().stream().map(groupMapper::toDto).collect(Collectors.toList());
        model.addAttribute("users", users);
        model.addAttribute("groups", groups);
        return "overseer/users";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userService.createUser(user);
        userService.updateUserGroups(user, userDto.getGroupIds());
        return "redirect:/overseer/users";
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userService.updateUser(user);
        userService.updateUserGroups(user, userDto.getGroupIds());
        return "redirect:/overseer/users";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUserById(userId);
        return "redirect:/overseer/users";
    }
}
