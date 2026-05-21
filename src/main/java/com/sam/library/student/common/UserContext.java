package com.sam.library.student.common;

import java.util.UUID;

public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<UUID> USER_UUID = new ThreadLocal<>();

    public static void set(Long id, UUID uuid) {
        USER_ID.set(id);
        USER_UUID.set(uuid);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static UUID getUserUuid() {
        return USER_UUID.get();
    }

    public static void clear() {
        USER_ID.remove();
        USER_UUID.remove();
    }
}
