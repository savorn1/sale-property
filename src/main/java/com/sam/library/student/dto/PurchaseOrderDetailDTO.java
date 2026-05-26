package com.sam.library.student.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderDetailDTO {
    private Long id;
    private Long purchaseOrderId;
    private Long productId;
    private String productName;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal total;
    private Integer receivedQty;
}
