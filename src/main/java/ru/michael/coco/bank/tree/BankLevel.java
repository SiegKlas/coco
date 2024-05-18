package ru.michael.coco.bank.tree;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class BankLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer levelNumber;

    @ManyToOne
    @JoinColumn(name = "bank_topic_id")
    @JsonBackReference
    private BankTopic bankTopic;

    @ElementCollection
    private List<String> exFiles = new ArrayList<>();
}