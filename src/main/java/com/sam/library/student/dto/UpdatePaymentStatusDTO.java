package com.sam.library.student.dto;

import com.sam.library.student.enums.PaymentStatus;
import lombok.Data;

@Data
public class UpdatePaymentStatusDTO {
    private PaymentStatus paymentStatus;
}
