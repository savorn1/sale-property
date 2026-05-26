package com.sam.library.student.service.impl;

import com.sam.library.student.entity.Brand;
import com.sam.library.student.entity.Category;
import com.sam.library.student.entity.Product;
import com.sam.library.student.exception.ResourceNotFoundException;
import com.sam.library.student.repository.BrandRepository;
import com.sam.library.student.repository.CategoryRepository;
import com.sam.library.student.repository.ProductRepository;
import com.sam.library.student.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getAllProducts(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            Specification<Product> spec = (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            return productRepository.findAll(spec, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public Product createProduct(Product product, Long categoryId, Long brandId) {
        String name = product.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }
        Product existing = productRepository.findByName(name).orElse(null);
        if (existing != null) {
            throw new IllegalArgumentException("Product with name '" + name + "' already exists.");
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", categoryId));
            product.setCategory(category);
        }
        if (brandId != null) {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));
            product.setBrand(brand);
        }
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public String updateProduct(Long id, Product incoming, Long brandId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        product.setName(incoming.getName());
        product.setDescription(incoming.getDescription());
        product.setPrice(incoming.getPrice());
        if (incoming.getMinStockLevel() != null) {
            product.setMinStockLevel(incoming.getMinStockLevel());
        }

        // Single image URL (original field)
        if (incoming.getImageUrl() != null) {
            product.setImageUrl(incoming.getImageUrl());
        }

        // Multiple image URLs (new field)
        if (incoming.getImageUrls() != null) {
            product.getImageUrls().clear();
            product.getImageUrls().addAll(incoming.getImageUrls());
        }

        if (brandId != null) {
            Brand brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));
            product.setBrand(brand);
        } else {
            product.setBrand(null);
        }

        productRepository.save(product);
        return "Product updated successfully.";
    }

    @Override
    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", id);
        }
        productRepository.deleteById(id);
        return "Product deleted successfully.";
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product", name));
    }
}
