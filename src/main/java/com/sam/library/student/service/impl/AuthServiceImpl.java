package com.sam.library.student.service.impl;

import com.sam.library.student.dto.LoginRequest;
import com.sam.library.student.dto.LoginResponse;
import com.sam.library.student.entity.SysUser;
import com.sam.library.student.repository.SysUserRepository;
import com.sam.library.student.service.AuthService;
import com.sam.library.student.util.JwtUtil;
import com.sam.library.student.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserRepository sysUserRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserRepository.findByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new IllegalArgumentException("Account is inactive.");
        }

        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getName());
        return new LoginResponse(token);
    }
}
