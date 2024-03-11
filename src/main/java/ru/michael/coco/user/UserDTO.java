package ru.michael.coco.user;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private User.Role role;
}
