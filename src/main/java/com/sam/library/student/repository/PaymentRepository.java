package com.sam.library.student.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sam.library.student.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByStatusAndCreatedAtBefore(String status, LocalDateTime cutoff);
}
