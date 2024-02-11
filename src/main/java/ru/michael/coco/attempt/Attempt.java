package ru.michael.coco.attempt;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Attempt {
    private final String solutionPath;
    @Embedded
    private final Response response;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
