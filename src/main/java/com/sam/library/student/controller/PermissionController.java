package com.sam.library.student.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.CreatePermissionDTO;
import com.sam.library.student.dto.PermissionDTO;
import com.sam.library.student.dto.PermissionFilterRequest;
import com.sam.library.student.mapper.PermissionMapper;
import com.sam.library.student.service.PermissionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/permissions")
@Tag(name = "Permission", description = "Permission management APIs")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<PermissionDTO>> getAll(@ModelAttribute PermissionFilterRequest filter) {
        Sort sort = filter.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);
        return ResponseEntity.ok(PageResponse.of(
                permissionService.getAll(filter, pageable).map(permissionMapper::toDTO)));
    }

    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(permissionMapper.toDTO(permissionService.getById(id))));
    }

    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PermissionDTO>> create(@RequestBody CreatePermissionDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Permission created",
                permissionMapper.toDTO(permissionService.create(dto))));
    }

    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionDTO>> update(@PathVariable Long id, @RequestBody CreatePermissionDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Permission updated",
                permissionMapper.toDTO(permissionService.update(id, dto))));
    }

    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(permissionService.delete(id)));
    }
}
