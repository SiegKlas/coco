package ru.michael.coco.group;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.michael.coco.bank.Bank;
import ru.michael.coco.user.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Table(name = "\"group\"")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToMany
    @JoinTable(
            name = "group_student",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<User> students = new ArrayList<>();

    @OneToOne
    private Bank activeBank;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bank> banks = new ArrayList<>();
}
