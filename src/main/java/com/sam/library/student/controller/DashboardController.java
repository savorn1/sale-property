package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.dto.dashboard.DashboardDTO;
import com.sam.library.student.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dashboard")
@Tag(name = "Dashboard", description = "Client-facing dashboard APIs")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Returns a single snapshot containing:
     * <ul>
     *   <li>Overview counts – total products, brands, categories, orders, total order amount</li>
     *   <li>Products by brand  – product count per brand</li>
     *   <li>Products by category – product count per category</li>
     *   <li>Order status summary – count + total amount per order status</li>
     *   <li>Payment chart – monthly payment totals grouped by status</li>
     * </ul>
     */
    @Operation(
        summary     = "Get dashboard data",
        description = "Returns overview metrics, product breakdowns by brand & category, "
                    + "order status summary, and a monthly payment chart grouped by payment status."
    )
    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardDTO>> getDashboard() {
        DashboardDTO dashboard = dashboardService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success(dashboard));
    }
}
