package com.sam.library.student.dto;

import lombok.Data;

@Data
public class SysUserDTO {
    private Long id;
    private String name;
    private String password;
    private String status;
}
