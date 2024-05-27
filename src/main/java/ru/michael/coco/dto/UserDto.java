package ru.michael.coco.dto;

import lombok.Data;
import ru.michael.coco.entity.User;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private User.Role role;
}
