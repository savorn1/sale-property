package com.sam.library.student.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CreateProductDTO {
    private String name;
    private String description;
    private Double price;
    private Long brandId;
    private Long categoryId;
    private String imageUrl;
    private List<String> imageUrls = new ArrayList<>();
}
