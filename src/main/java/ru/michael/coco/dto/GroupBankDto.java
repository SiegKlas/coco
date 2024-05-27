package ru.michael.coco.dto;

import lombok.Data;

@Data
public class GroupBankDto {
    private Long groupId;
    private Long bankId;
    private boolean isActive;
}
