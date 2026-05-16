package com.sam.library.student.enums;

import com.fasterxml.jackson.annotation.JsonAlias;

public enum ConversationType {
    @JsonAlias("DIRECT")
    PRIVATE,
    GROUP,
    BROADCAST
}
