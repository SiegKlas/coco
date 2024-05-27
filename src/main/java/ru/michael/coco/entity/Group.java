package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "\"group\"")
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupUser> groupUsers = new ArrayList<>();

    public void addGroupUser(GroupUser groupUser) {
        groupUsers.add(groupUser);
        groupUser.setGroup(this);
    }

    public void removeGroupUser(GroupUser groupUser) {
        groupUsers.remove(groupUser);
        groupUser.setGroup(null);
    }
}
