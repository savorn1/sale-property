package com.sam.library.student.service.impl;

import com.sam.library.student.common.UserContext;
import com.sam.library.student.dto.JwtUserClaims;
import com.sam.library.student.dto.LoginRequest;
import com.sam.library.student.dto.LoginResponse;
import com.sam.library.student.entity.SysUser;
import com.sam.library.student.redis.UserSessionStore;
import com.sam.library.student.repository.SysUserRepository;
import com.sam.library.student.service.AuthService;
import com.sam.library.student.util.JwtUtil;
import com.sam.library.student.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserRepository sysUserRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final UserSessionStore sessionStore;

    @Override
    public JwtUserClaims getProfile() {
        UUID uuid = UserContext.getUserUuid();
        if (uuid == null) throw new IllegalStateException("Not authenticated.");
        return sessionStore.find(uuid);
    }

    @Override
    public void logout() {
        UUID uuid = UserContext.getUserUuid();
        if (uuid == null) throw new IllegalStateException("Not authenticated.");
        sessionStore.remove(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserRepository.findWithRolesByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new IllegalArgumentException("Account is inactive.");
        }

        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> permission.getName())
                .distinct()
                .collect(Collectors.toList());

        JwtUserClaims claims = new JwtUserClaims(user.getId(), user.getName(), user.getUuid(), permissions);
        String token = jwtUtil.generateToken(claims);

        sessionStore.save(user.getUuid(), claims);

        return new LoginResponse(token);
    }
}
