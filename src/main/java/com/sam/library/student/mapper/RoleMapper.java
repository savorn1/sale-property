package com.sam.library.student.mapper;

import com.sam.library.student.dto.RoleDTO;
import com.sam.library.student.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final PermissionMapper permissionMapper;

    public RoleDTO toDTO(Role r) {
        RoleDTO dto = new RoleDTO();
        dto.setId(r.getId());
        dto.setName(r.getName());
        dto.setCode(r.getCode());
        dto.setDefault(r.isDefault());
        dto.setDescription(r.getDescription());
        if (r.getPermissions() != null) {
            dto.setPermissions(r.getPermissions().stream()
                    .map(permissionMapper::toDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
