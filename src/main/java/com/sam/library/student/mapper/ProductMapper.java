package com.sam.library.student.mapper;

import com.sam.library.student.dto.ProductDTO;
import com.sam.library.student.entity.Product;

import org.springframework.stereotype.Component;

import com.sam.library.student.dto.CreateProductDTO;

import java.util.ArrayList;

@Component
public class ProductMapper {

    public ProductDTO toProductDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setImageUrl(p.getImageUrl());
        dto.setImageUrls(p.getImageUrls() != null ? p.getImageUrls() : new ArrayList<>());
        dto.setStock(p.getStock());
        if (p.getBrand() != null) {
            dto.setBrandId(p.getBrand().getId());
            dto.setBrandName(p.getBrand().getName());
        }
        if (p.getCategory() != null) {
            dto.setCategoryId(p.getCategory().getId());
            dto.setCategoryName(p.getCategory().getName());
        }
        return dto;
    }

    public Product toProduct(CreateProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setImageUrls(dto.getImageUrls() != null ? dto.getImageUrls() : new ArrayList<>());
        return product;
    }
}
