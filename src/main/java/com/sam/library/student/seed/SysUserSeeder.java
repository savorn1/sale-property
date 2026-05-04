package com.sam.library.student.seed;

import com.sam.library.student.entity.SysUser;
import com.sam.library.student.repository.SysUserRepository;
import com.sam.library.student.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SysUserSeeder implements CommandLineRunner {

    private final SysUserRepository sysUserRepository;
    private final PasswordUtil passwordUtil;

    @Override
    public void run(String... args) {
        if (sysUserRepository.count() > 0) return;

        List<SysUser> users = List.of(
            build("admin",    "admin123",   "ACTIVE"),
            build("manager",  "manager123", "ACTIVE"),
            build("staff",    "staff123",   "ACTIVE"),
            build("guest",    "guest123",   "INACTIVE")
        );

        sysUserRepository.saveAll(users);
    }

    private SysUser build(String name, String password, String status) {
        SysUser user = new SysUser();
        user.setName(name);
        user.setPassword(passwordUtil.encode(password));
        user.setStatus(status);
        return user;
    }
}
