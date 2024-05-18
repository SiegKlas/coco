package ru.michael.coco.bank.tree;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class BankTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "bank_structure_id")
    private BankStructure bankStructure;

    @OneToMany(mappedBy = "bankTopic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankLevel> levels = new ArrayList<>();
}
