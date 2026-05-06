package com.sam.library.student.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    public void set(@NonNull String key, @NonNull Object value, long ttl, @NonNull TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, ttl, unit);
    }

    public void set(@NonNull String key, @NonNull Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Nullable
    public Object get(@NonNull String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Nullable
    public <T> T get(@NonNull String key, @NonNull Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? redisObjectMapper.convertValue(value, type) : null;
    }

    public boolean delete(@NonNull String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    public boolean hasKey(@NonNull String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public long getExpire(@NonNull String key, @NonNull TimeUnit unit) {
        Long ttl = redisTemplate.getExpire(key, unit);
        return ttl != null ? ttl : -1;
    }
}
