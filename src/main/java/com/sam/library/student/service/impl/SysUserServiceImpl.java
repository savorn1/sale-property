package com.sam.library.student.service.impl;

import com.sam.library.student.dto.SysUserFilterRequest;
import com.sam.library.student.entity.Role;
import com.sam.library.student.entity.SysUser;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.RoleRepository;
import com.sam.library.student.repository.SysUserRepository;
import com.sam.library.student.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.sam.library.student.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserRepository sysUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordUtil passwordUtil;

    @Override
    @Transactional(readOnly = true)
    public Page<SysUser> getAllSysUsers(SysUserFilterRequest filter, Pageable pageable) {
        Specification<SysUser> spec = Specification.where(null);

        if (filter.getName() != null && !filter.getName().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
        }

        if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("status"), filter.getStatus()));
        }

        if (filter.getStartDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getStartDate().atStartOfDay()));
        }

        if (filter.getEndDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), filter.getEndDate().atTime(23, 59, 59)));
        }

        Page<SysUser> result = sysUserRepository.findAll(spec, pageable);
        result.getContent().forEach(u -> Hibernate.initialize(u.getRoles()));
        return result;
    }

    @Override
    public SysUser getSysUserById(Long id) {
        return sysUserRepository.findWithRolesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SysUser", id));
    }

    @Override
    public SysUser createSysUser(SysUser sysUser) {
        if (sysUser.getName() == null || sysUser.getName().isBlank()) {
            throw new IllegalArgumentException("SysUser name cannot be null or empty.");
        }
        if (sysUser.getPassword() == null || sysUser.getPassword().isBlank()) {
            throw new IllegalArgumentException("SysUser password cannot be null or empty.");
        }
        if (sysUser.getStatus() == null || sysUser.getStatus().isBlank()) {
            throw new IllegalArgumentException("SysUser status cannot be null or empty.");
        }
        if (sysUserRepository.findByName(sysUser.getName()).isPresent()) {
            throw new IllegalArgumentException("SysUser with name '" + sysUser.getName() + "' already exists.");
        }
        sysUser.setPassword(passwordUtil.encode(sysUser.getPassword()));
        return sysUserRepository.save(sysUser);
    }

    @Override
    public SysUser updateSysUser(Long id, SysUser sysUser) {
        SysUser existing = sysUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SysUser", id));

        if (sysUser.getName() != null && !sysUser.getName().isBlank()) {
            sysUserRepository.findByName(sysUser.getName())
                    .filter(u -> !u.getId().equals(id))
                    .ifPresent(u -> { throw new IllegalArgumentException("SysUser with name '" + sysUser.getName() + "' already exists."); });
            existing.setName(sysUser.getName());
        }
        if (sysUser.getPassword() != null && !sysUser.getPassword().isBlank()) {
            existing.setPassword(passwordUtil.encode(sysUser.getPassword()));
        }
        if (sysUser.getStatus() != null && !sysUser.getStatus().isBlank()) {
            existing.setStatus(sysUser.getStatus());
        }
        return sysUserRepository.save(existing);
    }

    @Override
    public String deleteSysUser(Long id) {
        SysUser existing = sysUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SysUser", id));
        sysUserRepository.delete(existing);
        return "SysUser with id " + id + " has been deleted successfully.";
    }

    @Override
    @Transactional
    public SysUser assignRoles(Long id, List<Long> roleIds) {
        SysUser user = sysUserRepository.findWithRolesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SysUser", id));
        List<Role> roles = roleRepository.findAllById(roleIds);
        user.setRoles(new HashSet<>(roles));
        return sysUserRepository.save(user);
    }

    @Override
    public String deleteSysUsers(List<Long> ids) {
        List<SysUser> usersToDelete = sysUserRepository.findAllById(ids);
        if (usersToDelete.isEmpty()) {
            return "No users found for the provided IDs.";
        }
        sysUserRepository.deleteAll(usersToDelete);
        return "Deleted " + usersToDelete.size() + " users successfully.";
    }
}
