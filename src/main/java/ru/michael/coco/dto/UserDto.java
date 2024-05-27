package ru.michael.coco.dto;

import lombok.Data;
import ru.michael.coco.entity.User;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private User.Role role;
    private List<Long> groupIds;
}
