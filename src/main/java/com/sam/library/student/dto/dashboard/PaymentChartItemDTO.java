package com.sam.library.student.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentChartItemDTO {
    private int year;
    private int month;
    private String monthLabel;   // e.g. "Jan 2025"
    private String status;
    private Long count;
    private BigDecimal totalAmount;
}
