package com.sam.library.student.service;

import com.sam.library.student.entity.StockMovement;
import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.enums.StockMovementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockMovementService {

    /**
     * Programmatic stock IN — called by purchase order receive and open item confirm.
     */
    StockMovement stockIn(Long productId, int qty, StockMovementReason reason,
                          String referenceNo, Long referenceId, String remark);

    /**
     * Programmatic stock OUT — called by sales order creation.
     * Throws AppException(400) if insufficient stock.
     */
    StockMovement stockOut(Long productId, int qty, StockMovementReason reason,
                           String referenceNo, Long referenceId, String remark);

    Page<StockMovement> getMovements(Long productId, StockMovementType type, Pageable pageable);

    StockMovement getMovementById(Long id);
}
