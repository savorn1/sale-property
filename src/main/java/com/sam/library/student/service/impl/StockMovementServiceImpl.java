package com.sam.library.student.service.impl;

import com.sam.library.student.entity.Product;
import com.sam.library.student.entity.StockMovement;
import com.sam.library.student.enums.StockAlertType;
import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.enums.StockMovementType;
import com.sam.library.student.event.StockAlertEvent;
import com.sam.library.student.exception.AppException;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.ProductRepository;
import com.sam.library.student.repository.StockMovementRepository;
import com.sam.library.student.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public StockMovement stockIn(Long productId, int qty, StockMovementReason reason,
                                 String referenceNo, Long referenceId, String remark) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        int before = product.getStock() != null ? product.getStock() : 0;
        int after = before + qty;

        product.setStock(after);
        productRepository.save(product);

        StockMovement movement = saveMovement(product, StockMovementType.IN, reason, qty, before, after, referenceNo, referenceId, remark);

        // Fire RESTOCKED alert when stock recovers above the threshold
        int threshold = product.getMinStockLevel() != null ? product.getMinStockLevel() : 0;
        if (threshold > 0 && before <= threshold && after > threshold) {
            eventPublisher.publishEvent(new StockAlertEvent(
                    this, product.getId(), product.getName(), after, threshold, StockAlertType.RESTOCKED));
        }

        return movement;
    }

    @Override
    @Transactional
    public StockMovement stockOut(Long productId, int qty, StockMovementReason reason,
                                  String referenceNo, Long referenceId, String remark) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", productId));

        int before = product.getStock() != null ? product.getStock() : 0;
        if (before < qty) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Insufficient stock for product '" + product.getName() +
                    "'. Available: " + before + ", requested: " + qty);
        }

        int after = before - qty;
        product.setStock(after);
        productRepository.save(product);

        StockMovement movement = saveMovement(product, StockMovementType.OUT, reason, qty, before, after, referenceNo, referenceId, remark);

        // Fire LOW_STOCK alert when stock drops to or below the threshold
        int threshold = product.getMinStockLevel() != null ? product.getMinStockLevel() : 0;
        if (threshold > 0 && after <= threshold) {
            eventPublisher.publishEvent(new StockAlertEvent(
                    this, product.getId(), product.getName(), after, threshold, StockAlertType.LOW_STOCK));
        }

        return movement;
    }

    @Override
    public Page<StockMovement> getMovements(Long productId, StockMovementType type, Pageable pageable) {
        if (productId != null && type != null) {
            return stockMovementRepository.findByProductIdAndType(productId, type, pageable);
        }
        if (productId != null) {
            return stockMovementRepository.findByProductId(productId, pageable);
        }
        if (type != null) {
            return stockMovementRepository.findAll(
                    (root, query, cb) -> cb.equal(root.get("type"), type), pageable);
        }
        return stockMovementRepository.findAll(pageable);
    }

    @Override
    public StockMovement getMovementById(Long id) {
        return stockMovementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockMovement", id));
    }

    // -----------------------------------------------------------------------

    private StockMovement saveMovement(Product product, StockMovementType type,
                                       StockMovementReason reason, int qty,
                                       int before, int after,
                                       String referenceNo, Long referenceId, String remark) {
        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setType(type);
        movement.setReason(reason);
        movement.setQty(qty);
        movement.setBeforeQty(before);
        movement.setAfterQty(after);
        movement.setReferenceNo(referenceNo);
        movement.setReferenceId(referenceId);
        movement.setRemark(remark);
        return stockMovementRepository.save(movement);
    }
}
