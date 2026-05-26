package com.sam.library.student.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OpenItemStatus {
    DRAFT,
    CONFIRMED,
    CANCELLED;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static OpenItemStatus fromValue(String value) {
        for (OpenItemStatus s : values()) {
            if (s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid open item status: " + value);
    }
}
