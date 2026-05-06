package com.sam.library.student.mapper;

import com.sam.library.student.dto.CreatePermissionDTO;
import com.sam.library.student.dto.PermissionDTO;
import com.sam.library.student.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionDTO toDTO(Permission p) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setAction(p.getAction());
        dto.setDescription(p.getDescription());
        dto.setModule(p.getModule());
        return dto;
    }

    public Permission toEntity(CreatePermissionDTO dto) {
        Permission p = new Permission();
        p.setName(dto.getName());
        p.setAction(dto.getAction());
        p.setDescription(dto.getDescription());
        p.setModule(dto.getModule());
        return p;
    }
}
