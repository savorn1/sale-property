package com.sam.library.student.mapper;

import com.sam.library.student.dto.PaymentDTO;
import com.sam.library.student.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDTO toPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setPaymentNo(payment.getPaymentNo());
        dto.setOrderId(payment.getOrder().getId());
        dto.setOrderNo(payment.getOrder().getOrderNo());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaidAt(payment.getPaidAt());
        dto.setRemark(payment.getRemark());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}
