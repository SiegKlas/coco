package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Group;
import ru.michael.coco.entity.GroupUser;
import ru.michael.coco.entity.GroupUserId;
import ru.michael.coco.entity.User;
import ru.michael.coco.repository.GroupRepository;
import ru.michael.coco.repository.GroupUserRepository;
import ru.michael.coco.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, GroupUserRepository groupUserRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
        this.userRepository = userRepository;
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<Group> getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroupById(Long id) {
        groupRepository.deleteById(id);
    }

    public Group getOrCreateGroup(String groupName) {
        return groupRepository.findByName(groupName).orElseGet(() -> {
            Group group = new Group();
            group.setName(groupName);
            return groupRepository.save(group);
        });
    }

    public void addUserToGroup(User user, Group group) {
        GroupUserId groupUserId = new GroupUserId();
        groupUserId.setGroup(group);
        groupUserId.setUser(user);

        GroupUser groupUser = new GroupUser();
        groupUser.setId(groupUserId);
        groupUser.setGroup(group);
        groupUser.setUser(user);

        groupUserRepository.save(groupUser);
    }

    public void assignUsersToGroup(Group group, Long teacherId, List<Long> studentIds) {
        groupUserRepository.deleteAllByGroup(group);

        if (teacherId != null) {
            User teacher = userRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
            GroupUserId groupUserId = new GroupUserId();
            groupUserId.setGroup(group);
            groupUserId.setUser(teacher);

            GroupUser groupUser = new GroupUser();
            groupUser.setId(groupUserId);
            groupUser.setGroup(group);
            groupUser.setUser(teacher);
            groupUserRepository.save(groupUser);
        }

        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long studentId : studentIds) {
                User student = userRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));
                GroupUserId groupUserId = new GroupUserId();
                groupUserId.setGroup(group);
                groupUserId.setUser(student);

                GroupUser groupUser = new GroupUser();
                groupUser.setId(groupUserId);
                groupUser.setGroup(group);
                groupUser.setUser(student);
                groupUserRepository.save(groupUser);
            }
        }
    }
}
