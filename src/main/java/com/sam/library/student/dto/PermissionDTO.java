package com.sam.library.student.dto;

import lombok.Data;

@Data
public class PermissionDTO {
    private Long id;
    private String name;
    private String action;
    private String description;
    private String module;
}
