package com.sam.library.student.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PurchaseOrderStatus {
    DRAFT,
    ORDERED,
    PARTIALLY_RECEIVED,
    RECEIVED,
    CANCELLED;

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static PurchaseOrderStatus fromValue(String value) {
        for (PurchaseOrderStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid purchase order status: " + value);
    }
}
