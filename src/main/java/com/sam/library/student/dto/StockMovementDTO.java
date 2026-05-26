package com.sam.library.student.dto;

import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.enums.StockMovementType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockMovementDTO {
    private Long id;
    private Long productId;
    private String productName;
    private StockMovementType type;
    private StockMovementReason reason;
    private Integer qty;
    private Integer beforeQty;
    private Integer afterQty;
    private String referenceNo;
    private Long referenceId;
    private String remark;
    private LocalDateTime createdAt;
}
