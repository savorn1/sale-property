package com.sam.library.student.dto;

import com.sam.library.student.enums.OrderStatus;
import com.sam.library.student.enums.PaymentStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private String orderNo;
    private Long clientId;
    private String clientName;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentStatus paymentStatus;
    private String remark;
    private List<OrderDetailDTO> details;
    private List<PaymentDTO> payments;
}
