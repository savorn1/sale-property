package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.SysUserDTO;
import com.sam.library.student.dto.SysUserFilterRequest;
import com.sam.library.student.mapper.SysUserMapper;
import com.sam.library.student.service.SysUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/sys-user")
@Tag(name = "SysUser", description = "System user management APIs")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysUserMapper sysUserMapper;

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<SysUserDTO>> getAllSysUsers(@ModelAttribute SysUserFilterRequest filter) {
        Sort sort = filter.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);
        Page<SysUserDTO> result = sysUserService.getAllSysUsers(filter, pageable).map(sysUserMapper::toSysUserDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SysUserDTO>> getSysUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(sysUserMapper.toSysUserDTO(sysUserService.getSysUserById(id))));
    }

    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<SysUserDTO>> createSysUser(@RequestBody SysUserDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("SysUser created",
                sysUserMapper.toSysUserDTO(sysUserService.createSysUser(sysUserMapper.toSysUser(dto)))));
    }

    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SysUserDTO>> updateSysUser(@PathVariable Long id, @RequestBody SysUserDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("SysUser updated",
                sysUserMapper.toSysUserDTO(sysUserService.updateSysUser(id, sysUserMapper.toSysUser(dto)))));
    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSysUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(sysUserService.deleteSysUser(id)));
    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/delete-all")
    public ResponseEntity<ApiResponse<String>> deleteSysUsers(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(ApiResponse.success(sysUserService.deleteSysUsers(ids)));
    }
}
