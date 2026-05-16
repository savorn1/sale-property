package com.sam.library.student.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEventMessage {
    private Long paymentId;
    private String paymentNo;
    private String status;
    private BigDecimal amount;
    private LocalDateTime paidAt;
}
