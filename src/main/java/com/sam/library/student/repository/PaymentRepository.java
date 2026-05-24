package com.sam.library.student.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.sam.library.student.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    List<Payment> findByOrderId(Long orderId);

    List<Payment> findByStatusAndCreatedAtBefore(String status, LocalDateTime cutoff);

    /**
     * Monthly payment chart grouped by year, month and status.
     * Returns [year (Integer), month (Integer), status (String), count (Long), totalAmount (BigDecimal)] rows.
     * Covers both paid (paidAt) and unpaid/partial (createdAt fallback).
     */
    @Query("""
            SELECT YEAR(p.createdAt), MONTH(p.createdAt), p.status, COUNT(p), COALESCE(SUM(p.amount), 0)
            FROM Payment p
            GROUP BY YEAR(p.createdAt), MONTH(p.createdAt), p.status
            ORDER BY YEAR(p.createdAt), MONTH(p.createdAt), p.status
            """)
    List<Object[]> getPaymentChartData();
}
