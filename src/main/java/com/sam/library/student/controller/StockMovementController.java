package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.StockMovementDTO;
import com.sam.library.student.entity.StockMovement;
import com.sam.library.student.enums.StockMovementType;
import com.sam.library.student.mapper.StockMovementMapper;
import com.sam.library.student.service.StockMovementService;
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
@RequestMapping("api/stock-movement")
@Tag(name = "Stock Movement", description = "Read-only stock movement history APIs")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;
    private final StockMovementMapper stockMovementMapper;

    @PreAuthorize("hasAuthority('STOCK_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<StockMovementDTO>> getMovements(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by product ID")
            @RequestParam(required = false) Long productId,
            @Parameter(description = "Filter by type: IN or OUT")
            @RequestParam(required = false) StockMovementType type) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<StockMovementDTO> result = stockMovementService
                .getMovements(productId, type, pageable)
                .map(stockMovementMapper::toDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('STOCK_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockMovementDTO>> getMovementById(@PathVariable Long id) {
        StockMovement movement = stockMovementService.getMovementById(id);
        return ResponseEntity.ok(ApiResponse.success(stockMovementMapper.toDTO(movement)));
    }
}
