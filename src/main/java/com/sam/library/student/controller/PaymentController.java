package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.CreatePaymentDTO;
import com.sam.library.student.dto.PaymentDTO;
import com.sam.library.student.dto.UpdatePaymentStatusDTO;
import com.sam.library.student.entity.Payment;
import com.sam.library.student.enums.PaymentStatus;
import com.sam.library.student.mapper.PaymentMapper;
import com.sam.library.student.service.PaymentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
@Tag(name = "Payment", description = "Payment management APIs")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<PaymentDTO>> getAllPayments(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by payment status")
            @RequestParam(required = false) PaymentStatus status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PaymentDTO> result = paymentService.getAllPayments(status, pageable).map(paymentMapper::toPaymentDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success(paymentMapper.toPaymentDTO(payment)));
    }

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDTO>> createPayment(@RequestBody CreatePaymentDTO dto) {
        Payment payment = paymentService.createPayment(dto);
        return ResponseEntity.status(201).body(ApiResponse.success("Payment created", paymentMapper.toPaymentDTO(payment)));
    }

    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PaymentDTO>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody UpdatePaymentStatusDTO dto) {
        Payment payment = paymentService.updatePaymentStatus(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Payment status updated", paymentMapper.toPaymentDTO(payment)));
    }

    @PreAuthorize("hasAuthority('ORDER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(ApiResponse.success("Payment deleted with id: " + id));
    }
}
