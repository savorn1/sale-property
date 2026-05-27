package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.dto.SystemSettingDTO;
import com.sam.library.student.service.SystemSettingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/settings")
@Tag(name = "SystemSettings", description = "System-wide configuration")
@RequiredArgsConstructor
public class SystemSettingController {

    private final SystemSettingService settingService;

    /** Any authenticated user can read the current settings. */
    @GetMapping
    public ResponseEntity<ApiResponse<SystemSettingDTO>> getSettings() {
        return ResponseEntity.ok(ApiResponse.success(settingService.getSettings()));
    }

    /** Only admins (SETTING_UPDATE) may change settings. */
    @PreAuthorize("hasAuthority('SETTING_UPDATE')")
    @PutMapping
    public ResponseEntity<ApiResponse<SystemSettingDTO>> updateSettings(
            @RequestBody SystemSettingDTO dto) {
        SystemSettingDTO updated = settingService.updateSettings(dto);
        return ResponseEntity.ok(ApiResponse.success("Settings updated.", updated));
    }
}
