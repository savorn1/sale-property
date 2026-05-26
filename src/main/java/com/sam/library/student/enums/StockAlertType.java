package com.sam.library.student.enums;

public enum StockAlertType {
    /** Stock dropped to or below the configured minimum threshold. */
    LOW_STOCK,
    /** Stock recovered above the minimum threshold after a restock. */
    RESTOCKED
}
