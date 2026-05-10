package com.sam.library.student.service.impl;

import com.sam.library.student.dto.CreateOrderDTO;
import com.sam.library.student.dto.CreateOrderDetailDTO;
import com.sam.library.student.entity.Client;
import com.sam.library.student.entity.Order;
import com.sam.library.student.entity.OrderDetail;
import com.sam.library.student.entity.Product;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.ClientRepository;
import com.sam.library.student.repository.OrderRepository;
import com.sam.library.student.repository.ProductRepository;
import com.sam.library.student.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<Order> getAllOrders(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) {
            Specification<Order> spec = (root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("orderNo")), "%" + q.toLowerCase() + "%"),
                            cb.like(cb.lower(root.join("client").get("name")), "%" + q.toLowerCase() + "%")
                    );
            return orderRepository.findAll(spec, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
    }

    @Override
    @Transactional
    public Order createOrder(CreateOrderDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setClient(client);
        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDateTime.now());
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        order.setDiscount(dto.getDiscount() != null ? dto.getDiscount() : BigDecimal.ZERO);
        order.setTax(dto.getTax() != null ? dto.getTax() : BigDecimal.ZERO);
        order.setRemark(dto.getRemark());

        BigDecimal subtotal = BigDecimal.ZERO;

        if (dto.getDetails() != null) {
            for (CreateOrderDetailDTO detailDTO : dto.getDetails()) {
                Product product = productRepository.findById(detailDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product", detailDTO.getProductId()));

                OrderDetail detail = new OrderDetail();
                detail.setOrder(order);
                detail.setProduct(product);
                detail.setQty(detailDTO.getQty());
                detail.setPrice(detailDTO.getPrice());
                detail.setDiscount(detailDTO.getDiscount() != null ? detailDTO.getDiscount() : BigDecimal.ZERO);
                detail.setTotal(detailDTO.getTotal());

                order.getDetails().add(detail);
                subtotal = subtotal.add(detail.getTotal());
            }
        }

        order.setSubtotal(subtotal);
        order.setTotal(subtotal.subtract(order.getDiscount()).add(order.getTax()));

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", id);
        }
        orderRepository.deleteById(id);
    }

    private String generateOrderNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniquePart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "ORD-" + datePart + "-" + uniquePart;
    }
}
