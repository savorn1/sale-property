package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.CreateRoleDTO;
import com.sam.library.student.dto.RoleDTO;
import com.sam.library.student.dto.RoleFilterRequest;
import com.sam.library.student.mapper.RoleMapper;
import com.sam.library.student.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/roles")
@Tag(name = "Role", description = "Role management APIs")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<RoleDTO>> getAll(@ModelAttribute RoleFilterRequest filter) {
        Sort sort = filter.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);
        return ResponseEntity.ok(PageResponse.of(
                roleService.getAll(filter, pageable).map(roleMapper::toDTO)));
    }

    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roleMapper.toDTO(roleService.getById(id))));
    }

    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleDTO>> create(@RequestBody CreateRoleDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Role created",
                roleMapper.toDTO(roleService.create(dto))));
    }

    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDTO>> update(@PathVariable Long id, @RequestBody CreateRoleDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Role updated",
                roleMapper.toDTO(roleService.update(id, dto))));
    }

    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roleService.delete(id)));
    }
}
