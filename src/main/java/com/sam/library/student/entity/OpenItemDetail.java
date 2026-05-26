package com.sam.library.student.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "open_item_details")
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenItemDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "open_item_id", nullable = false)
    private OpenItem openItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer qty;

    @Column(columnDefinition = "TEXT")
    private String remark;
}
