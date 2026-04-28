package com.sam.library.student.service.impl;

import com.sam.library.student.entity.Product;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.ProductRepository;
import com.sam.library.student.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public String updateProduct(Long id, Product product) {
        int updated = productRepository.updateProductDetails(
                id,
                product.getName(),
                product.getDescription(),
                product.getPrice());
        if (updated == 0) throw new ResourceNotFoundException("Product", id);
        return "Product updated successfully.";
    }

    @Override
    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) throw new ResourceNotFoundException("Product", id);
        productRepository.deleteById(id);
        return "Product deleted successfully.";
    }
}
