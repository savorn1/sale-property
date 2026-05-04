package com.sam.library.student.mapper;

import com.sam.library.student.dto.SysUserDTO;
import com.sam.library.student.entity.SysUser;
import org.springframework.stereotype.Component;

@Component
public class SysUserMapper {

    public SysUserDTO toSysUserDTO(SysUser sysUser) {
        SysUserDTO dto = new SysUserDTO();
        dto.setId(sysUser.getId());
        dto.setName(sysUser.getName());
        dto.setPassword(sysUser.getPassword());
        dto.setStatus(sysUser.getStatus());
        return dto;
    }

    public SysUser toSysUser(SysUserDTO dto) {
        SysUser sysUser = new SysUser();
        sysUser.setName(dto.getName());
        sysUser.setPassword(dto.getPassword());
        sysUser.setStatus(dto.getStatus());
        return sysUser;
    }
}
