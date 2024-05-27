package ru.michael.coco.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.User;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupService;
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
    public void addUserToGroup(User user, Group group) {
        if (user.getRole() == User.Role.TEACHER) {
            group.setTeacher(user);
        } else if (user.getRole() == User.Role.STUDENT) {
            if (!group.getStudents().contains(user)) {
                group.getStudents().add(user);
            }
        }

        user.getGroups().add(group);

        groupService.saveGroup(group);
        userService.save(user);
    }

    @Transactional
    public void removeUserFromGroup(User user, Group group) {
        if (user.getRole() == User.Role.TEACHER && group.getTeacher() != null && group.getTeacher().equals(user)) {
            group.setTeacher(null);
        } else if (user.getRole() == User.Role.STUDENT) {
            group.getStudents().remove(user);
        }

        user.getGroups().remove(group);

        groupService.saveGroup(group);
        userService.save(user);
    }
}
