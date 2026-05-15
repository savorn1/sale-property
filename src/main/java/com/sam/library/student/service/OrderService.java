package com.sam.library.student.service;

import com.sam.library.student.dto.CreateOrderDTO;
import com.sam.library.student.dto.UpdateOrderStatusDTO;
import com.sam.library.student.dto.UpdatePaymentStatusDTO;
import com.sam.library.student.entity.Order;
import com.sam.library.student.enums.OrderStatus;
import com.sam.library.student.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {
    Page<Order> getAllOrders(String q, List<OrderStatus> status, List<PaymentStatus> paymentStatus, Pageable pageable);
    Order getOrderById(Long id);
    Order createOrder(CreateOrderDTO dto);
    Order updateOrderStatus(Long id, UpdateOrderStatusDTO dto);
    Order updatePaymentStatus(Long id, UpdatePaymentStatusDTO dto);
    void deleteOrder(Long id);
}
