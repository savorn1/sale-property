package com.sam.library.student.redis;

import org.springframework.lang.NonNull;

public final class RedisKey {

    private RedisKey() {}

    private static final String SESSION = "session:";
    private static final String REFRESH = "refresh:";
    private static final String USER_REFRESH = "user-refresh:";

    @NonNull
    public static String session(@NonNull String uuid) { return SESSION + uuid; }

    @NonNull
    public static String refresh(@NonNull String token) { return REFRESH + token; }

    @NonNull
    public static String userRefresh(@NonNull String uuid) { return USER_REFRESH + uuid; }

}
