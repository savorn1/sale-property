package com.sam.library.student.mapper;

import com.sam.library.student.dto.ProductDTO;
import com.sam.library.student.entity.Product;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    
    public ProductDTO toProductDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        return dto;
    }

    public Product toProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        return product;
    }
}
