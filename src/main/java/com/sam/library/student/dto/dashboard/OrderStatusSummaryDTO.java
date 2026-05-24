package com.sam.library.student.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusSummaryDTO {
    private String status;
    private Long count;
    private BigDecimal totalAmount;
}
