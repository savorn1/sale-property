package com.sam.library.student.repository;

import com.sam.library.student.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    /** Returns [status (String), count (Long), totalAmount (BigDecimal)] rows */
    @Query("SELECT o.status, COUNT(o), COALESCE(SUM(o.total), 0) FROM Order o GROUP BY o.status ORDER BY o.status")
    List<Object[]> getOrderStatusSummary();

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM Order o")
    BigDecimal getTotalOrderAmount();
}
