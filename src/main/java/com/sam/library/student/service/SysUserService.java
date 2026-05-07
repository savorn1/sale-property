package com.sam.library.student.service;

import com.sam.library.student.dto.SysUserFilterRequest;
import com.sam.library.student.entity.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SysUserService {
    Page<SysUser> getAllSysUsers(SysUserFilterRequest filter, Pageable pageable);
    SysUser getSysUserById(Long id);
    SysUser createSysUser(SysUser sysUser);
    SysUser updateSysUser(Long id, SysUser sysUser);
    String deleteSysUser(Long id);
    String deleteSysUsers(List<Long> ids);
    SysUser assignRoles(Long id, List<Long> roleIds);
}
