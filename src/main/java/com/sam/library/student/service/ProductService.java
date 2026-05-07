package com.sam.library.student.service;

import com.sam.library.student.entity.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    List<Product> getAllProducts();
    Page<Product> getAllProducts(Pageable pageable);
    Page<Product> getAllProducts(String name, Pageable pageable);
    Product getProductById(Long id);
    Product getProductByName(String name);
    Product createProduct(Product product, Long categoryId, Long brandId);
    String updateProduct(Long id, Product product, Long brandId);
    String deleteProduct(Long id);
}
