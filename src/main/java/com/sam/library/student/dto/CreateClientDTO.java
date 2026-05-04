package com.sam.library.student.dto;

import lombok.Data;

@Data
public class CreateClientDTO {
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String address;
}
