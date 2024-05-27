package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "\"group\"")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupUser> groupUsers = new ArrayList<>();

    public List<User> getUsers() {
        return groupUsers.stream()
                .map(GroupUser::getUser)
                .collect(Collectors.toList());
    }

    public void addGroupUser(GroupUser groupUser) {
        groupUsers.add(groupUser);
        groupUser.setGroup(this);
    }

    public void removeGroupUser(GroupUser groupUser) {
        groupUsers.remove(groupUser);
        groupUser.setGroup(null);
    }
}
