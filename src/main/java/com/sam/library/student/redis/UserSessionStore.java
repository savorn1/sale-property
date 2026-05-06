package com.sam.library.student.redis;

import com.sam.library.student.dto.JwtUserClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class UserSessionStore {

    private final RedisService redisService;

    @Value("${redis.expire}")
    private long sessionExpireHours;

    public UserSessionStore(RedisService redisService) {
        this.redisService = redisService;
    }

    public void save(@NonNull UUID uuid, @NonNull JwtUserClaims claims) {
        redisService.set(RedisKey.session(uuid.toString()), claims, sessionExpireHours, TimeUnit.HOURS);
    }

    @Nullable
    public JwtUserClaims find(@NonNull UUID uuid) {
        return redisService.get(RedisKey.session(uuid.toString()), JwtUserClaims.class);
    }

    public boolean remove(@NonNull UUID uuid) {
        return redisService.delete(RedisKey.session(uuid.toString()));
    }
}
