package com.sam.library.student.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    // ── Overview counts ──────────────────────────────────────────────────────
    private long totalProducts;
    private long totalBrands;
    private long totalCategories;
    private long totalOrders;
    private BigDecimal totalOrderAmount;

    // ── Products breakdown ───────────────────────────────────────────────────
    private List<BrandProductCountDTO> productsByBrand;
    private List<CategoryProductCountDTO> productsByCategory;

    // ── Orders breakdown by status ───────────────────────────────────────────
    private List<OrderStatusSummaryDTO> orderStatusSummary;

    // ── Payment chart (monthly, grouped by status) ───────────────────────────
    private List<PaymentChartItemDTO> paymentChart;
}
