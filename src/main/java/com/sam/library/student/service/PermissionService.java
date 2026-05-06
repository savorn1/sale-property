package com.sam.library.student.service;

import com.sam.library.student.dto.CreatePermissionDTO;
import com.sam.library.student.dto.PermissionFilterRequest;
import com.sam.library.student.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PermissionService {
    Page<Permission> getAll(PermissionFilterRequest filter, Pageable pageable);
    Permission getById(Long id);
    Permission create(CreatePermissionDTO dto);
    Permission update(Long id, CreatePermissionDTO dto);
    String delete(Long id);
}
