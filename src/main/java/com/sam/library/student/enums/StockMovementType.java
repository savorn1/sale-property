package com.sam.library.student.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StockMovementType {
    IN,
    OUT;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static StockMovementType fromValue(String value) {
        for (StockMovementType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid stock movement type: " + value);
    }
}
