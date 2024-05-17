package ru.michael.coco.user;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private User.Role role;
    private String email;
    private List<Long> groupIds;
    private List<String> groupNames;
}

