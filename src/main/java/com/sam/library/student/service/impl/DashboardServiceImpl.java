package com.sam.library.student.service.impl;

import com.sam.library.student.dto.dashboard.*;
import com.sam.library.student.repository.BrandRepository;
import com.sam.library.student.repository.CategoryRepository;
import com.sam.library.student.repository.OrderRepository;
import com.sam.library.student.repository.PaymentRepository;
import com.sam.library.student.repository.ProductRepository;
import com.sam.library.student.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public DashboardDTO getDashboard() {
        return DashboardDTO.builder()
                .totalProducts(productRepository.count())
                .totalBrands(brandRepository.count())
                .totalCategories(categoryRepository.count())
                .totalOrders(orderRepository.count())
                .totalOrderAmount(orderRepository.getTotalOrderAmount())
                .productsByBrand(buildBrandProductCounts())
                .productsByCategory(buildCategoryProductCounts())
                .orderStatusSummary(buildOrderStatusSummary())
                .paymentChart(buildPaymentChart())
                .build();
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private List<BrandProductCountDTO> buildBrandProductCounts() {
        return productRepository.countProductsByBrand().stream()
                .map(row -> new BrandProductCountDTO(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .toList();
    }

    private List<CategoryProductCountDTO> buildCategoryProductCounts() {
        return productRepository.countProductsByCategory().stream()
                .map(row -> new CategoryProductCountDTO(
                        (String) row[0],
                        ((Number) row[1]).longValue()))
                .toList();
    }

    private List<OrderStatusSummaryDTO> buildOrderStatusSummary() {
        return orderRepository.getOrderStatusSummary().stream()
                .map(row -> new OrderStatusSummaryDTO(
                        row[0].toString(),
                        ((Number) row[1]).longValue(),
                        row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO))
                .toList();
    }

    private List<PaymentChartItemDTO> buildPaymentChart() {
        return paymentRepository.getPaymentChartData().stream()
                .map(row -> {
                    int year  = ((Number) row[0]).intValue();
                    int month = ((Number) row[1]).intValue();
                    String label = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + year;
                    return new PaymentChartItemDTO(
                            year,
                            month,
                            label,
                            row[2].toString(),
                            ((Number) row[3]).longValue(),
                            row[4] != null ? new BigDecimal(row[4].toString()) : BigDecimal.ZERO);
                })
                .toList();
    }
}
