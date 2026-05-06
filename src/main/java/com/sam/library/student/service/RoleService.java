package com.sam.library.student.service;

import com.sam.library.student.dto.CreateRoleDTO;
import com.sam.library.student.dto.RoleFilterRequest;
import com.sam.library.student.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<Role> getAll(RoleFilterRequest filter, Pageable pageable);
    Role getById(Long id);
    Role create(CreateRoleDTO dto);
    Role update(Long id, CreateRoleDTO dto);
    String delete(Long id);
}
