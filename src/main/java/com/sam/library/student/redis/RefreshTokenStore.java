package com.sam.library.student.redis;

import com.sam.library.student.dto.JwtUserClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings("null")
public class RefreshTokenStore {

    private final RedisService redisService;

    @Value("${redis.refresh.expire}")
    private long refreshExpireHours;

    public RefreshTokenStore(RedisService redisService) {
        this.redisService = redisService;
    }

    @NonNull
    public String save(@NonNull UUID userUuid, @NonNull JwtUserClaims claims) {
        String uuidStr = userUuid.toString();
        String oldToken = redisService.get(RedisKey.userRefresh(uuidStr), String.class);
        if (oldToken != null) {
            redisService.delete(RedisKey.refresh(oldToken));
        }
        String token = Objects.requireNonNull(UUID.randomUUID().toString());
        redisService.set(RedisKey.refresh(token), claims, refreshExpireHours, TimeUnit.HOURS);
        redisService.set(RedisKey.userRefresh(uuidStr), token, refreshExpireHours, TimeUnit.HOURS);
        return token;
    }

    @Nullable
    public JwtUserClaims findClaims(@NonNull String token) {
        return redisService.get(RedisKey.refresh(token), JwtUserClaims.class);
    }

    @NonNull
    public String rotate(@NonNull String oldToken, @NonNull UUID userUuid, @NonNull JwtUserClaims claims) {
        redisService.delete(RedisKey.refresh(oldToken));
        return save(userUuid, claims);
    }

    public void remove(@NonNull UUID userUuid) {
        String uuidStr = userUuid.toString();
        String token = redisService.get(RedisKey.userRefresh(uuidStr), String.class);
        if (token != null) {
            redisService.delete(RedisKey.refresh(token));
        }
        redisService.delete(RedisKey.userRefresh(uuidStr));
    }
}
