package com.sam.library.student.service.impl;

import com.sam.library.student.dto.SystemSettingDTO;
import com.sam.library.student.entity.SystemSetting;
import com.sam.library.student.repository.SystemSettingRepository;
import com.sam.library.student.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements SystemSettingService {

    private static final String KEY_ALLOW_OVERSELLING = "allowOverselling";

    private final SystemSettingRepository repo;

    // -----------------------------------------------------------------------
    // Read
    // -----------------------------------------------------------------------

    @Override
    public SystemSettingDTO getSettings() {
        SystemSettingDTO dto = new SystemSettingDTO();
        dto.setAllowOverselling(getBool(KEY_ALLOW_OVERSELLING, false));
        return dto;
    }

    @Override
    public boolean isAllowOverselling() {
        return getBool(KEY_ALLOW_OVERSELLING, false);
    }

    // -----------------------------------------------------------------------
    // Write
    // -----------------------------------------------------------------------

    @Override
    @Transactional
    public SystemSettingDTO updateSettings(SystemSettingDTO dto) {
        set(KEY_ALLOW_OVERSELLING, dto.isAllowOverselling());
        return getSettings();
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private boolean getBool(String key, boolean defaultValue) {
        return repo.findById(key)
                .map(s -> Boolean.parseBoolean(s.getValue()))
                .orElse(defaultValue);
    }

    private void set(String key, boolean value) {
        repo.save(new SystemSetting(key, Boolean.toString(value)));
    }
}
