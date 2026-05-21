package com.sam.library.student.service;

import com.sam.library.student.dto.JwtUserClaims;
import com.sam.library.student.dto.LoginRequest;
import com.sam.library.student.dto.LoginResponse;
import com.sam.library.student.dto.RefreshTokenRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refresh(RefreshTokenRequest request);
    JwtUserClaims getProfile();
    void logout();
}
