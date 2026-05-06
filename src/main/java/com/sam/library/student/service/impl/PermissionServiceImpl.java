package com.sam.library.student.service.impl;

import com.sam.library.student.dto.CreatePermissionDTO;
import com.sam.library.student.dto.PermissionFilterRequest;
import com.sam.library.student.entity.Permission;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.PermissionRepository;
import com.sam.library.student.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Override
    public Page<Permission> getAll(PermissionFilterRequest filter, Pageable pageable) {
        Specification<Permission> spec = Specification.where(null);

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getAction() != null && !filter.getAction().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("action")), filter.getAction().toLowerCase()));
        }

        if (filter.getModule() != null && !filter.getModule().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(cb.lower(root.get("module")), filter.getModule().toLowerCase()));
        }

        return permissionRepository.findAll(spec, pageable);
    }

    @Override
    public Permission getById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission", id));
    }

    @Override
    public Permission create(CreatePermissionDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Permission name is required.");
        }
        if (dto.getAction() == null || dto.getAction().isBlank()) {
            throw new IllegalArgumentException("Permission action is required.");
        }
        if (dto.getModule() == null || dto.getModule().isBlank()) {
            throw new IllegalArgumentException("Permission module is required.");
        }
        if (permissionRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Permission with name '" + dto.getName() + "' already exists.");
        }

        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setAction(dto.getAction());
        permission.setDescription(dto.getDescription());
        permission.setModule(dto.getModule());
        return permissionRepository.save(permission);
    }

    @Override
    public Permission update(Long id, CreatePermissionDTO dto) {
        Permission permission = getById(id);

        if (dto.getName() != null && !dto.getName().isBlank()) {
            if (!dto.getName().equals(permission.getName()) && permissionRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Permission with name '" + dto.getName() + "' already exists.");
            }
            permission.setName(dto.getName());
        }
        if (dto.getAction() != null && !dto.getAction().isBlank()) {
            permission.setAction(dto.getAction());
        }
        if (dto.getModule() != null && !dto.getModule().isBlank()) {
            permission.setModule(dto.getModule());
        }
        permission.setDescription(dto.getDescription());
        return permissionRepository.save(permission);
    }

    @Override
    public String delete(Long id) {
        Permission permission = getById(id);
        permissionRepository.delete(permission);
        return "Permission with id " + id + " has been deleted successfully.";
    }
}
