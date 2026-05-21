package com.sam.library.student.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private Long id;
    private String studentId;
    private String name;
    private String photo;
    private String phoneNumber;
    private String email;
    private String createdAt;
    private String updatedAt;
}
