package com.sam.library.student.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.CreateOrderDTO;
import com.sam.library.student.dto.OrderDTO;
import com.sam.library.student.dto.UpdateOrderStatusDTO;
import com.sam.library.student.dto.UpdatePaymentStatusDTO;
import com.sam.library.student.entity.Order;
import com.sam.library.student.enums.OrderStatus;
import com.sam.library.student.enums.PaymentStatus;
import com.sam.library.student.mapper.OrderMapper;
import com.sam.library.student.service.OrderService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/order")
@Tag(name = "Order", description = "Order management APIs")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<OrderDTO>> getAllOrders(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search by order no or client name")
            @RequestParam(required = false) String q,
            @Parameter(description = "Filter by order status (multiple allowed)")
            @RequestParam(required = false) List<OrderStatus> status,
            @Parameter(description = "Filter by payment status (multiple allowed)")
            @RequestParam(required = false) List<PaymentStatus> paymentStatus) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<OrderDTO> result = orderService.getAllOrders(q, status, paymentStatus, pageable).map(orderMapper::toOrderDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(orderMapper.toOrderDTO(order)));
    }

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@RequestBody CreateOrderDTO dto) {
        Order order = orderService.createOrder(dto);
        return ResponseEntity.status(201).body(ApiResponse.success("Order created", orderMapper.toOrderDTO(order)));
    }

    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusDTO dto) {
        Order order = orderService.updateOrderStatus(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", orderMapper.toOrderDTO(order)));
    }

    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    @PatchMapping("/{id}/payment-status")
    public ResponseEntity<ApiResponse<OrderDTO>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody UpdatePaymentStatusDTO dto) {
        Order order = orderService.updatePaymentStatus(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Payment status updated", orderMapper.toOrderDTO(order)));
    }

    @PreAuthorize("hasAuthority('ORDER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order deleted with id: " + id));
    }
}
