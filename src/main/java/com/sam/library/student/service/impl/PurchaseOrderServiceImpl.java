package com.sam.library.student.service.impl;

import com.sam.library.student.dto.CreatePurchaseOrderDTO;
import com.sam.library.student.dto.CreatePurchaseOrderDetailDTO;
import com.sam.library.student.dto.ReceivePurchaseOrderDTO;
import com.sam.library.student.dto.UpdatePurchaseOrderStatusDTO;
import com.sam.library.student.entity.Product;
import com.sam.library.student.entity.PurchaseOrder;
import com.sam.library.student.entity.PurchaseOrderDetail;
import com.sam.library.student.entity.Supplier;
import com.sam.library.student.enums.PurchaseOrderStatus;
import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.event.DashboardChangedEvent;
import com.sam.library.student.exception.AppException;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.ProductRepository;
import com.sam.library.student.repository.PurchaseOrderRepository;
import com.sam.library.student.repository.SupplierRepository;
import com.sam.library.student.service.PurchaseOrderService;
import com.sam.library.student.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Page<PurchaseOrder> getAllPurchaseOrders(String q, List<PurchaseOrderStatus> status, Pageable pageable) {
        Specification<PurchaseOrder> spec = Specification.where(null);

        if (q != null && !q.isBlank()) {
            String pattern = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("poNo")), pattern),
                    cb.like(cb.lower(root.join("supplier").get("name")), pattern)
            ));
        }

        if (status != null && !status.isEmpty()) {
            List<String> statusNames = status.stream().map(Enum::name).toList();
            spec = spec.and((root, query, cb) -> root.get("status").in(statusNames));
        }

        return purchaseOrderRepository.findAll(spec, pageable);
    }

    @Override
    public PurchaseOrder getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", id));
    }

    @Override
    @Transactional
    public PurchaseOrder createPurchaseOrder(CreatePurchaseOrderDTO dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", dto.getSupplierId()));

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNo(generatePoNo());
        po.setSupplier(supplier);
        po.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDateTime.now());
        po.setExpectedDate(dto.getExpectedDate());
        po.setStatus(dto.getStatus() != null ? dto.getStatus() : PurchaseOrderStatus.DRAFT);
        po.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO);
        po.setTax(dto.getTax() != null ? dto.getTax() : BigDecimal.ZERO);
        po.setRemark(dto.getRemark());

        BigDecimal subtotal = BigDecimal.ZERO;

        if (dto.getDetails() != null) {
            for (CreatePurchaseOrderDetailDTO detailDTO : dto.getDetails()) {
                Product product = productRepository.findById(detailDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product", detailDTO.getProductId()));

                BigDecimal discount = detailDTO.getDiscount() != null ? detailDTO.getDiscount() : BigDecimal.ZERO;
                BigDecimal lineTotal = detailDTO.getPrice()
                        .multiply(BigDecimal.valueOf(detailDTO.getQty()))
                        .subtract(discount);

                PurchaseOrderDetail detail = new PurchaseOrderDetail();
                detail.setPurchaseOrder(po);
                detail.setProduct(product);
                detail.setQty(detailDTO.getQty());
                detail.setPrice(detailDTO.getPrice());
                detail.setDiscount(discount);
                detail.setTotal(lineTotal);
                detail.setReceivedQty(0);

                po.getDetails().add(detail);
                subtotal = subtotal.add(lineTotal);
            }
        }

        po.setSubtotal(subtotal);
        po.setTotal(subtotal.subtract(po.getDiscount()).add(po.getTax()));

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        eventPublisher.publishEvent(new DashboardChangedEvent(this));
        return saved;
    }

    @Override
    @Transactional
    public PurchaseOrder updatePurchaseOrderStatus(Long id, UpdatePurchaseOrderStatusDTO dto) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", id));

        if (po.getStatus() == PurchaseOrderStatus.CANCELLED) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Cannot change status of a cancelled purchase order");
        }

        if (dto.getStatus() != null) {
            po.setStatus(dto.getStatus());
        }

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        eventPublisher.publishEvent(new DashboardChangedEvent(this));
        return saved;
    }

    @Override
    @Transactional
    public PurchaseOrder receiveItems(Long id, ReceivePurchaseOrderDTO dto) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", id));

        if (po.getStatus() == PurchaseOrderStatus.CANCELLED) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Cannot receive items for a cancelled purchase order");
        }
        if (po.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Purchase order is already fully received");
        }

        Map<Long, PurchaseOrderDetail> detailMap = po.getDetails().stream()
                .collect(Collectors.toMap(PurchaseOrderDetail::getId, Function.identity()));

        for (ReceivePurchaseOrderDTO.ReceiveItemDTO item : dto.getItems()) {
            PurchaseOrderDetail detail = detailMap.get(item.getDetailId());
            if (detail == null) {
                throw new ResourceNotFoundException("PurchaseOrderDetail", item.getDetailId());
            }
            int newReceived = detail.getReceivedQty() + item.getReceivedQty();
            if (newReceived > detail.getQty()) {
                throw new AppException(HttpStatus.BAD_REQUEST,
                        "Received qty exceeds ordered qty for product: " + detail.getProduct().getName());
            }
            detail.setReceivedQty(newReceived);

            // Record stock IN for the quantity received in this call
            stockMovementService.stockIn(
                    detail.getProduct().getId(),
                    item.getReceivedQty(),
                    StockMovementReason.PURCHASE,
                    po.getPoNo(),
                    po.getId(),
                    null
            );
        }

        // Auto-update status based on received quantities
        boolean allReceived = po.getDetails().stream()
                .allMatch(d -> d.getReceivedQty().equals(d.getQty()));
        boolean anyReceived = po.getDetails().stream()
                .anyMatch(d -> d.getReceivedQty() > 0);

        if (allReceived) {
            po.setStatus(PurchaseOrderStatus.RECEIVED);
        } else if (anyReceived) {
            po.setStatus(PurchaseOrderStatus.PARTIALLY_RECEIVED);
        }

        PurchaseOrder saved = purchaseOrderRepository.save(po);
        eventPublisher.publishEvent(new DashboardChangedEvent(this));
        return saved;
    }

    @Override
    @Transactional
    public void deletePurchaseOrder(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PurchaseOrder", id));

        if (po.getStatus() != PurchaseOrderStatus.DRAFT && po.getStatus() != PurchaseOrderStatus.CANCELLED) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Only DRAFT or CANCELLED purchase orders can be deleted");
        }

        purchaseOrderRepository.deleteById(id);
        eventPublisher.publishEvent(new DashboardChangedEvent(this));
    }

    private String generatePoNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniquePart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "PO-" + datePart + "-" + uniquePart;
    }
}
