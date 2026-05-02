package com.sam.library.student.service;

import com.sam.library.student.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product getProductByName(String name);
    Product createProduct(Product product, Long categoryId, Long brandId);
    String updateProduct(Long id, Product product, Long brandId);
    String deleteProduct(Long id);
}
