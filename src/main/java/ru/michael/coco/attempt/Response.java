package ru.michael.coco.attempt;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Response {
    private final String status;
    @Column(length = 10000)
    private final String output;
    private final String error;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
