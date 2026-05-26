package com.sam.library.student.dto;

import com.sam.library.student.enums.StockAlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * WebSocket payload broadcast to {@code /topic/stock-alerts} whenever a
 * product's stock crosses its low-stock threshold.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockAlertDTO {

    private Long productId;
    private String productName;
    private int currentStock;
    private int minStockLevel;

    /**
     * {@link StockAlertType#LOW_STOCK} when stock reached or fell below
     * the threshold; {@link StockAlertType#RESTOCKED} when it recovered.
     */
    private StockAlertType alertType;

    /** ISO-8601 timestamp of when the alert was generated. */
    private Instant triggeredAt = Instant.now();
}
