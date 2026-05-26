package com.sam.library.student.dto;

import com.sam.library.student.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateOrderDTO {
    private Long clientId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal discount;
    private BigDecimal tax;
    private String remark;
    private List<CreateOrderDetailDTO> details;
}
