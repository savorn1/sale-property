package com.sam.library.student.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePurchaseOrderDetailDTO {
    private Long productId;
    private Integer qty;
    private BigDecimal price;
    private BigDecimal discount;
}
