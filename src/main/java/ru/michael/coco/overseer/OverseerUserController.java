package ru.michael.coco.overseer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.michael.coco.configs.GroupAssignmentService;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupDTO;
import ru.michael.coco.group.GroupMapper;
import ru.michael.coco.group.GroupService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserDTO;
import ru.michael.coco.user.UserMapper;
import ru.michael.coco.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/overseer/users")
public class OverseerUserController {
    private final UserService userService;
    private final GroupService groupService;
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;
    private final GroupAssignmentService groupAssignmentService;

    @Autowired
    public OverseerUserController(UserService userService, GroupService groupService, UserMapper userMapper, GroupMapper groupMapper, GroupAssignmentService groupAssignmentService) {
        this.userService = userService;
        this.groupService = groupService;
        this.userMapper = userMapper;
        this.groupMapper = groupMapper;
        this.groupAssignmentService = groupAssignmentService;
    }

    @GetMapping
    public String getUsersPage(Model model) {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream().map(userMapper::toDTO).collect(Collectors.toList());
        List<Group> groups = groupService.findAllGroups();
        List<GroupDTO> groupDTOs = groups.stream().map(groupMapper::toDTO).collect(Collectors.toList());
        model.addAttribute("users", userDTOs);
        model.addAttribute("groups", groupDTOs);
        return "overseer/users";
    }

    @PostMapping("/create")
    @Transactional
    public String createUser(@ModelAttribute UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

        if (userDTO.getGroupIds() != null && !userDTO.getGroupIds().isEmpty()) {
            List<Group> groups = userDTO.getGroupIds().stream()
                    .map(groupId -> groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found")))
                    .collect(Collectors.toList());
            user.setGroups(groups);
        }

        userService.save(user);

        if (user.getGroups() != null && !user.getGroups().isEmpty()) {
            List<Group> groupsToUpdate = new ArrayList<>(user.getGroups());
            for (Group group : groupsToUpdate) {
                groupAssignmentService.addUserToGroup(user, group);
            }
        }

        return "redirect:/overseer/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute UserDTO userDTO) {
        User user = userService.findById(userDTO.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user.setEmail(userDTO.getEmail());
        if (!userDTO.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        }

        if (userDTO.getGroupIds() != null) {
            List<Group> groups = userDTO.getGroupIds().stream()
                    .map(groupId -> groupService.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found")))
                    .collect(Collectors.toList());
            user.setGroups(groups);
        } else {
            user.getGroups().clear();
        }

        userService.save(user);
        return "redirect:/overseer/users";
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<String> deleteUser(@RequestParam Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
