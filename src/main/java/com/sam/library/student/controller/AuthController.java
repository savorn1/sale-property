package com.sam.library.student.controller;

import com.sam.library.student.annotation.RateLimit;
import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.dto.JwtUserClaims;
import com.sam.library.student.dto.LoginRequest;
import com.sam.library.student.dto.LoginResponse;
import com.sam.library.student.dto.RefreshTokenRequest;
import com.sam.library.student.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Auth", description = "Authentication APIs")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @RateLimit(max = 5, window = 60)
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<JwtUserClaims>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success(authService.getProfile()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(ApiResponse.success(authService.refresh(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
