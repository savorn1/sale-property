package com.sam.library.student.service;

import com.sam.library.student.dto.SystemSettingDTO;

public interface SystemSettingService {

    /** Returns all current settings. */
    SystemSettingDTO getSettings();

    /** Persists all settings from the DTO. */
    SystemSettingDTO updateSettings(SystemSettingDTO dto);

    /** Convenience: is overselling currently allowed? */
    boolean isAllowOverselling();
}
