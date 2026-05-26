package com.sam.library.student.dto;

import com.sam.library.student.enums.PurchaseOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreatePurchaseOrderDTO {
    private Long supplierId;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDate;
    private PurchaseOrderStatus status;
    private BigDecimal discount;
    private BigDecimal tax;
    private String remark;
    private List<CreatePurchaseOrderDetailDTO> details;
}
