package com.sam.library.student.dto;

import com.sam.library.student.enums.OrderStatus;

import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    private OrderStatus status;
}
