package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.entity.Group;
import ru.michael.coco.entity.GroupUser;
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

    public void assignUsersToGroup(Group group, Long teacherId, List<Long> studentIds) {
        // Получаем текущие записи GroupUser для данной группы
        List<GroupUser> currentGroupUsers = groupUserRepository.findByGroup(group);

        // Удаляем старые записи GroupUser
        groupUserRepository.deleteAll(currentGroupUsers);

        // Добавляем новую запись для учителя, если он есть
        if (teacherId != null) {
            User teacher = userRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
            GroupUser groupUser = new GroupUser(group, teacher);
            groupUserRepository.save(groupUser);
        }

        // Добавляем новые записи для студентов, если они есть
        if (studentIds != null && !studentIds.isEmpty()) {
            for (Long studentId : studentIds) {
                User student = userRepository.findById(studentId).orElseThrow(() -> new IllegalArgumentException("Student not found"));
                GroupUser groupUser = new GroupUser(group, student);
                groupUserRepository.save(groupUser);
            }
        }
    }

    public void addUserToGroup(User user, Group group) {
        GroupUser groupUser = new GroupUser(group, user);
        groupUserRepository.save(groupUser);
    }
}
