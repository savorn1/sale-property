package com.sam.library.student.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sam.library.student.dto.JwtUserClaims;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final MacAlgorithm ALGORITHM = Jwts.SIG.HS256;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(JwtUserClaims claims) {
        Map<String, Object> user = Map.of(
                "id", claims.getId(),
                "username", claims.getUsername(),
                "uuid", claims.getUuid().toString()
        );
        return Jwts.builder()
                .claim("user", user)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), ALGORITHM)
                .compact();
    }

    public String extractUsername(String token) {
        return extractUserClaim(token, "username", String.class);
    }

    public Long extractId(String token) {
        return extractUserClaim(token, "id", Long.class);
    }

    public UUID extractUuid(String token) {
        String uuid = extractUserClaim(token, "uuid", String.class);
        return UUID.fromString(uuid);
    }

    public JwtUserClaims extractUserClaims(String token) {
        return new JwtUserClaims(extractId(token), extractUsername(token), extractUuid(token), List.of());
    }

    @SuppressWarnings("unchecked")
    private <T> T extractUserClaim(String token, String key, Class<T> type) {
        return extractClaim(token, claims -> {
            Map<String, Object> user = claims.get("user", Map.class);
            return type.cast(user.get(key));
        });
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
