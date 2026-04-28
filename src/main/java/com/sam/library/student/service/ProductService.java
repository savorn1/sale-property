package com.sam.library.student.service;

import com.sam.library.student.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product createProduct(Product product);
    String updateProduct(Long id, Product product);
    String deleteProduct(Long id);
}
