package ru.michael.coco.bank.tree;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.michael.coco.bank.Bank;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class BankStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bank_id")
    @JsonBackReference
    private Bank bank;

    @OneToMany(mappedBy = "bankStructure", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BankTopic> topics = new ArrayList<>();
}


