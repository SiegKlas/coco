package ru.michael.coco.attempt;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Response {
    private final String status;
    private final String output;
    private final String error;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonCreator
    public Response(@JsonProperty("status") String status,
                    @JsonProperty("output") String output,
                    @JsonProperty("error") String error) {
        this.status = status;
        this.output = output;
        this.error = error;
    }
}
