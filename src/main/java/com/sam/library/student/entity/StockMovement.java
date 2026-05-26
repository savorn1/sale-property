package com.sam.library.student.entity;

import com.sam.library.student.enums.StockMovementReason;
import com.sam.library.student.enums.StockMovementType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "stock_movements")
@Data
@EqualsAndHashCode(callSuper = true)
public class StockMovement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private StockMovementType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private StockMovementReason reason;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "before_qty", nullable = false)
    private Integer beforeQty;

    @Column(name = "after_qty", nullable = false)
    private Integer afterQty;

    /** PO number / order number that triggered this movement */
    @Column(name = "reference_no", length = 50)
    private String referenceNo;

    /** ID of the PurchaseOrder or Order that triggered this movement */
    @Column(name = "reference_id")
    private Long referenceId;

    @Column(columnDefinition = "TEXT")
    private String remark;
}
