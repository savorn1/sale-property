package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.CreatePurchaseOrderDTO;
import com.sam.library.student.dto.PurchaseOrderDTO;
import com.sam.library.student.dto.ReceivePurchaseOrderDTO;
import com.sam.library.student.dto.UpdatePurchaseOrderStatusDTO;
import com.sam.library.student.entity.PurchaseOrder;
import com.sam.library.student.enums.PurchaseOrderStatus;
import com.sam.library.student.mapper.PurchaseOrderMapper;
import com.sam.library.student.service.PurchaseOrderService;
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

import java.util.List;

@RestController
@RequestMapping("api/purchase-order")
@Tag(name = "Purchase Order", description = "Purchase Order management APIs")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @PreAuthorize("hasAuthority('PURCHASE_ORDER_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<PurchaseOrderDTO>> getAllPurchaseOrders(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Search by PO number or supplier name")
            @RequestParam(required = false) String q,
            @Parameter(description = "Filter by status (multiple allowed)")
            @RequestParam(required = false) List<PurchaseOrderStatus> status) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PurchaseOrderDTO> result = purchaseOrderService
                .getAllPurchaseOrders(q, status, pageable)
                .map(purchaseOrderMapper::toPurchaseOrderDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('PURCHASE_ORDER_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PurchaseOrderDTO>> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrder po = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(ApiResponse.success(purchaseOrderMapper.toPurchaseOrderDTO(po)));
    }

    @PreAuthorize("hasAuthority('PURCHASE_ORDER_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseOrderDTO>> createPurchaseOrder(
            @RequestBody CreatePurchaseOrderDTO dto) {
        PurchaseOrder po = purchaseOrderService.createPurchaseOrder(dto);
        return ResponseEntity.status(201)
                .body(ApiResponse.success("Purchase order created", purchaseOrderMapper.toPurchaseOrderDTO(po)));
    }

    @PreAuthorize("hasAuthority('PURCHASE_ORDER_UPDATE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<PurchaseOrderDTO>> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdatePurchaseOrderStatusDTO dto) {
        PurchaseOrder po = purchaseOrderService.updatePurchaseOrderStatus(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Purchase order status updated",
                purchaseOrderMapper.toPurchaseOrderDTO(po)));
    }

    @PreAuthorize("hasAuthority('PURCHASE_ORDER_UPDATE')")
    @PatchMapping("/{id}/receive")
    public ResponseEntity<ApiResponse<PurchaseOrderDTO>> receiveItems(
            @PathVariable Long id,
            @RequestBody ReceivePurchaseOrderDTO dto) {
        PurchaseOrder po = purchaseOrderService.receiveItems(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Items received",
                purchaseOrderMapper.toPurchaseOrderDTO(po)));
    }

    @PreAuthorize("hasAuthority('PURCHASE_ORDER_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order deleted with id: " + id));
    }
}
