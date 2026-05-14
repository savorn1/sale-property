package com.sam.library.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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
}
