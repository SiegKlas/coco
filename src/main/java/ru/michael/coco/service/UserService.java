package ru.michael.coco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupUserRepository groupUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, GroupRepository groupRepository, GroupUserRepository groupUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllTeachers() {
        return userRepository.findByRole(User.Role.TEACHER);
    }

    public List<User> getAllStudents() {
        return userRepository.findByRole(User.Role.STUDENT);
    }

    public void updateUserGroups(User user, List<Long> groupIds) {
        User managedUser = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        managedUser.getGroupUsers().clear();
        userRepository.save(managedUser);

        if (groupIds != null && !groupIds.isEmpty()) {
            for (Long groupId : groupIds) {
                Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
                GroupUser groupUser = new GroupUser(group, managedUser);
                managedUser.addGroupUser(groupUser);
            }
        }

        userRepository.save(managedUser);
    }
}
