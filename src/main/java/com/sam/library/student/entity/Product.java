package com.sam.library.student.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = true)
    private String description;

    @Column(nullable = false)
    private Double price = 0.0;

    @Column
    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_image_urls", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @OrderColumn(name = "idx")
    private List<String> imageUrls = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer stock = 0;

    /**
     * Low-stock threshold. A {@code StockAlertEvent} is fired whenever
     * {@code stock <= minStockLevel} after a stock-out, and a RESTOCKED
     * alert is fired when stock rises back above the threshold after a
     * stock-in. Defaults to 0 (alerts disabled).
     */
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer minStockLevel = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    // //Getter and Setter for name
    // public String getName() {
    //     return name;
    // }

    // public void setName(String name) {
    //     this.name = name;
    // }

    // // Getter and Setter for description
    // public String getDescription() {
    //     return description;
    // }

    // public void setDescription(String description) {
    //     this.description = description;
    // }

    // // Getter and Setter for price
    // public Double getPrice() {
    //     return price;
    // }

    // public void setPrice(Double price) {
    //     this.price = price;
    // }

}
