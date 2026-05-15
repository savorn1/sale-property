package com.sam.library.student.mapper;

import com.sam.library.student.dto.OrderDTO;
import com.sam.library.student.dto.OrderDetailDTO;
import com.sam.library.student.dto.PaymentDTO;
import com.sam.library.student.entity.Order;
import com.sam.library.student.entity.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final PaymentMapper paymentMapper;

    public OrderDTO toOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setClientId(order.getClient().getId());
        dto.setClientName(order.getClient().getName());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setSubtotal(order.getSubtotal());
        dto.setDiscount(order.getDiscount());
        dto.setTax(order.getTax());
        dto.setTotal(order.getTotal());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setRemark(order.getRemark());
        List<OrderDetailDTO> details = order.getDetails().stream()
                .map(this::toDetailDTO)
                .collect(Collectors.toList());
        dto.setDetails(details);
        List<PaymentDTO> payments = order.getPayments().stream()
                .map(paymentMapper::toPaymentDTO)
                .collect(Collectors.toList());
        dto.setPayments(payments);
        return dto;
    }

    public OrderDetailDTO toDetailDTO(OrderDetail d) {
        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(d.getId());
        dto.setOrderId(d.getOrder().getId());
        dto.setProductId(d.getProduct().getId());
        dto.setProductName(d.getProduct().getName());
        dto.setQty(d.getQty());
        dto.setPrice(d.getPrice());
        dto.setDiscount(d.getDiscount());
        dto.setTotal(d.getTotal());
        return dto;
    }
}
