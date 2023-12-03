package ru.michael.coco.bank.tree;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private BankStructure bankStructure;

    @OneToMany(mappedBy = "bankTopic", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BankLevel> levels = new ArrayList<>();
}
