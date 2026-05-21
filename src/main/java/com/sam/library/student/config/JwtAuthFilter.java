package com.sam.library.student.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.jsonwebtoken.JwtException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sam.library.student.common.UserContext;
import com.sam.library.student.dto.JwtUserClaims;
import com.sam.library.student.redis.UserSessionStore;
import com.sam.library.student.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserSessionStore sessionStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        UUID uuid;
        try {
            uuid = jwtUtil.extractUuid(token);
        } catch (JwtException | IllegalArgumentException e) {
            chain.doFilter(request, response);
            return;
        }

        if (uuid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            JwtUserClaims claims = sessionStore.find(uuid);
            if (claims != null) {
                UserContext.set(claims.getId(), claims.getUuid());
                List<SimpleGrantedAuthority> authorities = claims.getPermissions() == null
                        ? List.of()
                        : claims.getPermissions().stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList();
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                claims.getUsername(),
                                null,
                                authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
