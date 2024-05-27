package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class GroupUser {
    @EmbeddedId
    private GroupUserId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
