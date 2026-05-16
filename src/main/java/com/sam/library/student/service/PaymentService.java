package com.sam.library.student.service;

import com.sam.library.student.dto.CreatePaymentDTO;
import com.sam.library.student.dto.UpdatePaymentStatusDTO;
import com.sam.library.student.entity.Payment;
import com.sam.library.student.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Page<Payment> getAllPayments(PaymentStatus status, Pageable pageable);
    Payment getPaymentById(Long id);
    Payment createPayment(CreatePaymentDTO dto);
    Payment updatePaymentStatus(Long id, UpdatePaymentStatusDTO dto);
    void deletePayment(Long id);
    void autoMarkExpiredPaymentsAsPaid();
}
