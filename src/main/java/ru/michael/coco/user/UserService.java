package ru.michael.coco.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.michael.coco.group.Group;
import ru.michael.coco.group.GroupRepository;
import ru.michael.coco.group.GroupService;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;

    @Autowired
    public UserService(UserRepository userRepository, GroupRepository groupRepository, GroupService groupService) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupService = groupService;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteAll() {
        this.userRepository.deleteAll();
    }

    public void save(User user) {
        this.userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllByRole(User.Role role) {
        return userRepository.findAllByRole(role);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        for (Group group : user.getGroups()) {
            group.getStudents().remove(user);
            if (group.getTeacher() != null && group.getTeacher().equals(user)) {
                group.setTeacher(null);
            }
            groupRepository.save(group);
        }

        userRepository.deleteById(id);
    }

    public void assignUserToGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));

        group.getStudents().add(user);
        groupRepository.save(group);
    }
}
