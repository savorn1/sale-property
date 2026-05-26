package com.sam.library.student.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StockMovementReason {
    // IN reasons
    PURCHASE,               // received from purchase order
    RETURN_FROM_CUSTOMER,   // customer returned goods
    INITIAL_STOCK,          // initial stock setup
    ADJUSTMENT_IN,          // manual positive adjustment

    // OUT reasons
    SALE,                   // sold via sales order
    RETURN_TO_SUPPLIER,     // returned to supplier
    DAMAGE,                 // damaged / written off
    ADJUSTMENT_OUT;         // manual negative adjustment

    @JsonValue
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static StockMovementReason fromValue(String value) {
        for (StockMovementReason reason : values()) {
            if (reason.name().equalsIgnoreCase(value)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Invalid stock movement reason: " + value);
    }
}
