package com.sam.library.student.mapper;

import com.sam.library.student.dto.StockMovementDTO;
import com.sam.library.student.entity.StockMovement;
import org.springframework.stereotype.Component;

@Component
public class StockMovementMapper {

    public StockMovementDTO toDTO(StockMovement m) {
        StockMovementDTO dto = new StockMovementDTO();
        dto.setId(m.getId());
        dto.setProductId(m.getProduct().getId());
        dto.setProductName(m.getProduct().getName());
        dto.setType(m.getType());
        dto.setReason(m.getReason());
        dto.setQty(m.getQty());
        dto.setBeforeQty(m.getBeforeQty());
        dto.setAfterQty(m.getAfterQty());
        dto.setReferenceNo(m.getReferenceNo());
        dto.setReferenceId(m.getReferenceId());
        dto.setRemark(m.getRemark());
        dto.setCreatedAt(m.getCreatedAt());
        return dto;
    }
}
