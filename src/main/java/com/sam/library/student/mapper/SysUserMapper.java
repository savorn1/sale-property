package com.sam.library.student.mapper;

import com.sam.library.student.dto.SysUserDTO;
import com.sam.library.student.entity.SysUser;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class SysUserMapper {

    public SysUserDTO toSysUserDTO(SysUser sysUser) {
        SysUserDTO dto = new SysUserDTO();
        dto.setId(sysUser.getId());
        dto.setName(sysUser.getName());
        dto.setStatus(sysUser.getStatus());
        dto.setRoleIds(sysUser.getRoles() == null ? Collections.emptyList() :
                sysUser.getRoles().stream().map(r -> r.getId()).collect(Collectors.toList()));
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
