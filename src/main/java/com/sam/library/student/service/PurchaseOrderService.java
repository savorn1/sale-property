package com.sam.library.student.service;

import com.sam.library.student.dto.CreatePurchaseOrderDTO;
import com.sam.library.student.dto.ReceivePurchaseOrderDTO;
import com.sam.library.student.dto.UpdatePurchaseOrderStatusDTO;
import com.sam.library.student.entity.PurchaseOrder;
import com.sam.library.student.enums.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseOrderService {
    Page<PurchaseOrder> getAllPurchaseOrders(String q, List<PurchaseOrderStatus> status, Pageable pageable);
    PurchaseOrder getPurchaseOrderById(Long id);
    PurchaseOrder createPurchaseOrder(CreatePurchaseOrderDTO dto);
    PurchaseOrder updatePurchaseOrderStatus(Long id, UpdatePurchaseOrderStatusDTO dto);
    PurchaseOrder receiveItems(Long id, ReceivePurchaseOrderDTO dto);
    void deletePurchaseOrder(Long id);
}
