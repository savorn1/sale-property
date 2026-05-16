package com.sam.library.student.repository;

import com.sam.library.student.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByStatusAndCreatedAtBefore(String status, LocalDateTime cutoff);
}
