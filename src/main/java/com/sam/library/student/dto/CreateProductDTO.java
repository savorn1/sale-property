package com.sam.library.student.dto;
import lombok.Data;

@Data
public class CreateProductDTO {
    private String name;
    private String description;
    private Double price;
    private Long brandId;
    private Long categoryId;

}
