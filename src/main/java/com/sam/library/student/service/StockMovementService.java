package com.sam.library.student.service;

import com.sam.library.student.dto.CreateStockMovementDTO;
import com.sam.library.student.entity.StockMovement;
import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.enums.StockMovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockMovementService {

    /** Manual stock adjustment (via REST) */
    StockMovement createMovement(CreateStockMovementDTO dto);

    /**
     * Programmatic stock IN — called internally by purchase order receive.
     * Returns the saved StockMovement.
     */
    StockMovement stockIn(Long productId, int qty, StockMovementReason reason,
                          String referenceNo, Long referenceId, String remark);

    /**
     * Programmatic stock OUT — called internally by sales order creation.
     * Throws AppException if insufficient stock.
     */
    StockMovement stockOut(Long productId, int qty, StockMovementReason reason,
                           String referenceNo, Long referenceId, String remark);

    Page<StockMovement> getMovements(Long productId, StockMovementType type, Pageable pageable);

    StockMovement getMovementById(Long id);
}
