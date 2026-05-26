package com.sam.library.student.dto;

import com.sam.library.student.enums.PurchaseOrderStatus;
import lombok.Data;

@Data
public class UpdatePurchaseOrderStatusDTO {
    private PurchaseOrderStatus status;
}
