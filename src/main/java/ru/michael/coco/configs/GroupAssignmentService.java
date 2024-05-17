package ru.michael.coco.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupService;
import ru.michael.coco.user.User;
import ru.michael.coco.user.UserService;

@Service
public class GroupAssignmentService {
    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupAssignmentService(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @Transactional
    public void addUserToGroup(User user, String groupName) {
        Group group = groupService.findByName(groupName)
                .orElseGet(() -> {
                    Group newGroup = new Group();
                    newGroup.setName(groupName);
                    groupService.saveGroup(newGroup);
                    return newGroup;
                });

        if (user.getRole() == User.Role.TEACHER) {
            group.setTeacher(user);
        } else if (user.getRole() == User.Role.STUDENT) {
            group.getStudents().add(user);
        }

        user.getGroups().add(group);

        groupService.saveGroup(group);
        userService.save(user);
    }
}
