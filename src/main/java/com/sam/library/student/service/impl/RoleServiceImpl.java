package com.sam.library.student.service.impl;

import com.sam.library.student.dto.CreateRoleDTO;
import com.sam.library.student.dto.RoleFilterRequest;
import com.sam.library.student.entity.Permission;
import com.sam.library.student.entity.Role;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.PermissionRepository;
import com.sam.library.student.repository.RoleRepository;
import com.sam.library.student.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Page<Role> getAll(RoleFilterRequest filter, Pageable pageable) {
        Specification<Role> spec = Specification.where(null);

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getCode() != null && !filter.getCode().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("code")), "%" + filter.getCode().toLowerCase() + "%"));
        }

        if (filter.getIsDefault() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("isDefault"), filter.getIsDefault()));
        }

        return roleRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional
    public Role getById(Long id) {
        return roleRepository.findWithPermissionsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
    }

    @Override
    @Transactional
    public Role create(CreateRoleDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Role name is required.");
        }
        if (dto.getCode() == null || dto.getCode().isBlank()) {
            throw new IllegalArgumentException("Role code is required.");
        }
        if (roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Role with name '" + dto.getName() + "' already exists.");
        }
        if (roleRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Role with code '" + dto.getCode() + "' already exists.");
        }

        Role role = new Role();
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        role.setDefault(dto.isDefault());
        role.setDescription(dto.getDescription());
        role.setPermissions(resolvePermissions(dto.getPermissionIds()));
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role update(Long id, CreateRoleDTO dto) {
        Role role = roleRepository.findWithPermissionsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            if (!dto.getName().equals(role.getName()) && roleRepository.existsByName(dto.getName())) {
                throw new IllegalArgumentException("Role with name '" + dto.getName() + "' already exists.");
            }
            role.setName(dto.getName());
        }

        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            if (!dto.getCode().equals(role.getCode()) && roleRepository.existsByCode(dto.getCode())) {
                throw new IllegalArgumentException("Role with code '" + dto.getCode() + "' already exists.");
            }
            role.setCode(dto.getCode());
        }

        role.setDefault(dto.isDefault());
        role.setDescription(dto.getDescription());

        if (dto.getPermissionIds() != null) {
            role.setPermissions(resolvePermissions(dto.getPermissionIds()));
        }

        return roleRepository.save(role);
    }

    @Override
    public String delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
        roleRepository.delete(role);
        return "Role with id " + id + " has been deleted successfully.";
    }

    private Set<Permission> resolvePermissions(List<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new HashSet<>();
        }
        List<Permission> found = permissionRepository.findAllById(permissionIds);
        if (found.size() != permissionIds.size()) {
            throw new IllegalArgumentException("One or more permission IDs are invalid.");
        }
        return new HashSet<>(found);
    }
}
