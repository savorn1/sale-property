package com.sam.library.student.service;

import com.sam.library.student.dto.JwtUserClaims;
import com.sam.library.student.dto.LoginRequest;
import com.sam.library.student.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    JwtUserClaims getProfile();
    void logout();
}
