package com.sam.library.student.service.impl;

import com.sam.library.student.dto.CreatePaymentDTO;
import com.sam.library.student.dto.UpdatePaymentStatusDTO;
import com.sam.library.student.entity.Order;
import com.sam.library.student.entity.Payment;
import com.sam.library.student.enums.PaymentStatus;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.OrderRepository;
import com.sam.library.student.repository.PaymentRepository;
import com.sam.library.student.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    public Page<Payment> getAllPayments(PaymentStatus status, Pageable pageable) {
        Specification<Payment> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status.name()));
        }

        return paymentRepository.findAll(spec, pageable);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
    }

    @Override
    @Transactional
    public Payment createPayment(CreatePaymentDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", dto.getOrderId()));

        Payment payment = new Payment();
        payment.setPaymentNo(generatePaymentNo());
        payment.setOrder(order);
        payment.setAmount(dto.getAmount() != null ? dto.getAmount() : order.getTotal());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus(dto.getStatus() != null ? dto.getStatus() : PaymentStatus.UNPAID.name());
        payment.setPaidAt(dto.getPaidAt());
        payment.setRemark(dto.getRemark());

        Payment saved = paymentRepository.save(payment);
        syncOrderPaymentStatus(order);
        return saved;
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(Long id, UpdatePaymentStatusDTO dto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));

        if (dto.getPaymentStatus() != null) {
            payment.setStatus(dto.getPaymentStatus().name());
            if (dto.getPaymentStatus() == PaymentStatus.PAID && payment.getPaidAt() == null) {
                payment.setPaidAt(LocalDateTime.now());
            }
        }

        Payment saved = paymentRepository.save(payment);
        syncOrderPaymentStatus(payment.getOrder());
        return saved;
    }

    @Override
    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", id));
        Order order = payment.getOrder();
        paymentRepository.deleteById(id);
        syncOrderPaymentStatus(order);
    }

    // Recalculates order.paymentStatus based on all its payments
    private void syncOrderPaymentStatus(Order order) {
        List<Payment> payments = paymentRepository.findByOrderId(order.getId());

        BigDecimal totalPaid = payments.stream()
                .filter(p -> PaymentStatus.PAID.name().equals(p.getStatus()))
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String newStatus;
        if (totalPaid.compareTo(order.getTotal()) >= 0) {
            newStatus = PaymentStatus.PAID.name();
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            newStatus = PaymentStatus.PARTIAL.name();
        } else {
            boolean allRefunded = !payments.isEmpty() &&
                    payments.stream().allMatch(p -> PaymentStatus.REFUNDED.name().equals(p.getStatus()));
            newStatus = allRefunded ? PaymentStatus.REFUNDED.name() : PaymentStatus.UNPAID.name();
        }

        order.setPaymentStatus(newStatus);
        orderRepository.save(order);
    }

    private String generatePaymentNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniquePart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "PAY-" + datePart + "-" + uniquePart;
    }
}
