package com.sam.library.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;
    private String name;
    private String code;
    private boolean isDefault;
    private String description;
    private List<PermissionDTO> permissions;
}
