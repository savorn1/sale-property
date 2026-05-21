package com.sam.library.student.service.impl;

import com.sam.library.student.common.UserContext;
import com.sam.library.student.dto.JwtUserClaims;
import com.sam.library.student.dto.LoginRequest;
import com.sam.library.student.dto.LoginResponse;
import com.sam.library.student.dto.RefreshTokenRequest;
import com.sam.library.student.redis.RefreshTokenStore;
import com.sam.library.student.redis.UserSessionStore;
import com.sam.library.student.repository.SysUserRepository;
import com.sam.library.student.service.AuthService;
import com.sam.library.student.util.JwtUtil;
import com.sam.library.student.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AuthServiceImpl implements AuthService {

    private final SysUserRepository sysUserRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final UserSessionStore sessionStore;
    private final RefreshTokenStore refreshTokenStore;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        var user = sysUserRepository.findWithRolesByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new IllegalArgumentException("Account is inactive.");
        }

        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        List<String> permissions = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(p -> p.getName())
                .distinct()
                .collect(Collectors.toList());

        JwtUserClaims claims = new JwtUserClaims(user.getId(), user.getName(), user.getUuid(), permissions);
        String accessToken = jwtUtil.generateToken(claims);
        String refreshToken = refreshTokenStore.save(user.getUuid(), claims);

        sessionStore.save(user.getUuid(), claims);

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    public LoginResponse refresh(RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        JwtUserClaims claims = refreshTokenStore.findClaims(token);
        if (claims == null) {
            throw new IllegalArgumentException("Invalid or expired refresh token.");
        }

        UUID userUuid = claims.getUuid();
        String newAccessToken = jwtUtil.generateToken(claims);
        String newRefreshToken = refreshTokenStore.rotate(token, userUuid, claims);

        sessionStore.save(userUuid, claims);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public JwtUserClaims getProfile() {
        UUID uuid = UserContext.getUserUuid();
        if (uuid == null) {
            throw new IllegalStateException("Not authenticated.");
        }
        return sessionStore.find(uuid);
    }

    @Override
    public void logout() {
        UUID uuid = UserContext.getUserUuid();
        if (uuid == null) {
            throw new IllegalStateException("Not authenticated.");
        }
        sessionStore.remove(uuid);
        refreshTokenStore.remove(uuid);
    }
}
