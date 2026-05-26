package com.sam.library.student.mapper;

import com.sam.library.student.dto.PurchaseOrderDTO;
import com.sam.library.student.dto.PurchaseOrderDetailDTO;
import com.sam.library.student.entity.PurchaseOrder;
import com.sam.library.student.entity.PurchaseOrderDetail;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrderDTO toPurchaseOrderDTO(PurchaseOrder po) {
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(po.getId());
        dto.setPoNo(po.getPoNo());
        dto.setSupplierId(po.getSupplier().getId());
        dto.setSupplierName(po.getSupplier().getName());
        dto.setOrderDate(po.getOrderDate());
        dto.setExpectedDate(po.getExpectedDate());
        dto.setStatus(po.getStatus());
        dto.setSubtotal(po.getSubtotal());
        dto.setDiscount(po.getDiscount());
        dto.setTax(po.getTax());
        dto.setTotal(po.getTotal());
        dto.setRemark(po.getRemark());
        dto.setCreatedAt(po.getCreatedAt());
        dto.setUpdatedAt(po.getUpdatedAt());

        List<PurchaseOrderDetailDTO> details = po.getDetails().stream()
                .map(this::toDetailDTO)
                .collect(Collectors.toList());
        dto.setDetails(details);

        return dto;
    }

    public PurchaseOrderDetailDTO toDetailDTO(PurchaseOrderDetail d) {
        PurchaseOrderDetailDTO dto = new PurchaseOrderDetailDTO();
        dto.setId(d.getId());
        dto.setPurchaseOrderId(d.getPurchaseOrder().getId());
        dto.setProductId(d.getProduct().getId());
        dto.setProductName(d.getProduct().getName());
        dto.setQty(d.getQty());
        dto.setPrice(d.getPrice());
        dto.setDiscount(d.getDiscount());
        dto.setTotal(d.getTotal());
        dto.setReceivedQty(d.getReceivedQty());
        return dto;
    }
}
