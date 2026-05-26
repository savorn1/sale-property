package com.sam.library.student.service.impl;

import com.sam.library.student.dto.CreateOpenItemDTO;
import com.sam.library.student.dto.CreateOpenItemDetailDTO;
import com.sam.library.student.entity.OpenItem;
import com.sam.library.student.entity.OpenItemDetail;
import com.sam.library.student.entity.Product;
import com.sam.library.student.enums.OpenItemStatus;
import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.exception.AppException;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.OpenItemRepository;
import com.sam.library.student.repository.ProductRepository;
import com.sam.library.student.service.OpenItemService;
import com.sam.library.student.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OpenItemServiceImpl implements OpenItemService {

    private final OpenItemRepository openItemRepository;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;

    // ─── Queries ─────────────────────────────────────────────────────────────

    @Override
    public Page<OpenItem> getAll(String q, List<OpenItemStatus> status, Pageable pageable) {
        Specification<OpenItem> spec = Specification.where(null);

        if (q != null && !q.isBlank()) {
            String pattern = "%" + q.toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("openItemNo")), pattern));
        }

        if (status != null && !status.isEmpty()) {
            List<String> names = status.stream().map(Enum::name).toList();
            spec = spec.and((root, query, cb) -> root.get("status").in(names));
        }

        return openItemRepository.findAll(spec, pageable);
    }

    @Override
    public OpenItem getById(Long id) {
        return openItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OpenItem", id));
    }

    // ─── Commands ────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public OpenItem create(CreateOpenItemDTO dto) {
        OpenItem openItem = new OpenItem();
        openItem.setOpenItemNo(generateOpenItemNo());
        openItem.setItemDate(dto.getItemDate() != null ? dto.getItemDate() : LocalDate.now());
        openItem.setStatus(OpenItemStatus.DRAFT);
        openItem.setRemark(dto.getRemark());

        for (CreateOpenItemDetailDTO detailDTO : dto.getDetails()) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", detailDTO.getProductId()));

            OpenItemDetail detail = new OpenItemDetail();
            detail.setOpenItem(openItem);
            detail.setProduct(product);
            detail.setQty(detailDTO.getQty());
            detail.setRemark(detailDTO.getRemark());

            openItem.getDetails().add(detail);
        }

        return openItemRepository.save(openItem);
    }

    @Override
    @Transactional
    public OpenItem confirm(Long id) {
        OpenItem openItem = openItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OpenItem", id));

        if (openItem.getStatus() != OpenItemStatus.DRAFT) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Only DRAFT open items can be confirmed. Current status: " + openItem.getStatus());
        }

        if (openItem.getDetails().isEmpty()) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Cannot confirm an open item with no details");
        }

        // Create INITIAL_STOCK movement for every detail line
        for (OpenItemDetail detail : openItem.getDetails()) {
            stockMovementService.stockIn(
                    detail.getProduct().getId(),
                    detail.getQty(),
                    StockMovementReason.INITIAL_STOCK,
                    openItem.getOpenItemNo(),
                    openItem.getId(),
                    detail.getRemark() != null ? detail.getRemark() : "Opening stock"
            );
        }

        openItem.setStatus(OpenItemStatus.CONFIRMED);
        return openItemRepository.save(openItem);
    }

    @Override
    @Transactional
    public OpenItem cancel(Long id) {
        OpenItem openItem = openItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OpenItem", id));

        if (openItem.getStatus() == OpenItemStatus.CANCELLED) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Open item is already cancelled");
        }

        // If it was confirmed, reverse the stock that was added
        if (openItem.getStatus() == OpenItemStatus.CONFIRMED) {
            for (OpenItemDetail detail : openItem.getDetails()) {
                stockMovementService.stockOut(
                        detail.getProduct().getId(),
                        detail.getQty(),
                        StockMovementReason.ADJUSTMENT_OUT,
                        openItem.getOpenItemNo(),
                        openItem.getId(),
                        "Open item cancelled"
                );
            }
        }

        openItem.setStatus(OpenItemStatus.CANCELLED);
        return openItemRepository.save(openItem);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        OpenItem openItem = openItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OpenItem", id));

        if (openItem.getStatus() != OpenItemStatus.DRAFT) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Only DRAFT open items can be deleted");
        }

        openItemRepository.deleteById(id);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private String generateOpenItemNo() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniquePart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "OI-" + datePart + "-" + uniquePart;
    }
}
