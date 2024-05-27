package ru.michael.coco.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class GroupBank {
    @EmbeddedId
    private GroupBankId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("bankId")
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "is_active")
    private boolean isActive;
}
