package com.sam.library.student.event;

import com.sam.library.student.enums.StockAlertType;
import org.springframework.context.ApplicationEvent;

/**
 * Published by {@link com.sam.library.student.service.impl.StockMovementServiceImpl}
 * whenever a product's stock crosses its {@code minStockLevel} threshold.
 *
 * <ul>
 *   <li>{@link StockAlertType#LOW_STOCK} – stock dropped to or below the threshold after a stock-out.</li>
 *   <li>{@link StockAlertType#RESTOCKED} – stock recovered above the threshold after a stock-in.</li>
 * </ul>
 */
public class StockAlertEvent extends ApplicationEvent {

    private final Long productId;
    private final String productName;
    private final int currentStock;
    private final int minStockLevel;
    private final StockAlertType alertType;

    public StockAlertEvent(Object source,
                           Long productId,
                           String productName,
                           int currentStock,
                           int minStockLevel,
                           StockAlertType alertType) {
        super(source);
        this.productId = productId;
        this.productName = productName;
        this.currentStock = currentStock;
        this.minStockLevel = minStockLevel;
        this.alertType = alertType;
    }

    public Long getProductId()      { return productId; }
    public String getProductName()  { return productName; }
    public int getCurrentStock()    { return currentStock; }
    public int getMinStockLevel()   { return minStockLevel; }
    public StockAlertType getAlertType() { return alertType; }
}
