package com.sam.library.student.redis;

import org.springframework.lang.NonNull;

public final class RedisKey {

    private RedisKey() {}

    private static final String SESSION = "session:";

    @NonNull
    public static String session(@NonNull String uuid) { return SESSION + uuid; }

}
