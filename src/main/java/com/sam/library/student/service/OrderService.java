package com.sam.library.student.service;

import com.sam.library.student.dto.CreateOrderDTO;
import com.sam.library.student.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<Order> getAllOrders(String q, Pageable pageable);
    Order getOrderById(Long id);
    Order createOrder(CreateOrderDTO dto);
    void deleteOrder(Long id);
}
