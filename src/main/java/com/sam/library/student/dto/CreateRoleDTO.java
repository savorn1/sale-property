package com.sam.library.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoleDTO {
    private String name;
    private String code;
    private boolean isDefault;
    private String description;
    private List<Long> permissionIds;
}
