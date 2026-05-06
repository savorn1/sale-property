package com.sam.library.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUserClaims {
    private Long id;
    private String username;
    private UUID uuid;
    private List<String> permissions;
}
