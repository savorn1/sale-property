package com.sam.library.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private String imageUrl;
    private List<String> imageUrls = new ArrayList<>();
    private Integer stock;
    /** Low-stock threshold configured on this product. */
    private Integer minStockLevel;
}
