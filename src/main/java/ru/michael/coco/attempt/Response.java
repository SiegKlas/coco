package ru.michael.coco.attempt;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Response {
    private final String status;
    @Column(length = 10000)
    private final String output;
    private final String error;
}
