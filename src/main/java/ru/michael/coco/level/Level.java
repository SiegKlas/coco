package ru.michael.coco.level;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import ru.michael.coco.level_description.LevelDescription;
import ru.michael.coco.user.User;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Level {
    @ManyToOne
    private final User user;
    @ManyToOne
    private final LevelDescription levelDescription;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nullable
    private Integer pass = null;
}
