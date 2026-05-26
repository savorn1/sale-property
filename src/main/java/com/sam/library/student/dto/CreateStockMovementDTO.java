package com.sam.library.student.dto;

import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.enums.StockMovementType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStockMovementDTO {

    @NotNull
    private Long productId;

    @NotNull
    private StockMovementType type;

    @NotNull
    private StockMovementReason reason;

    @NotNull
    @Min(1)
    private Integer qty;

    private String referenceNo;
    private String remark;
}
