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
    public String updateProduct(Long id, Product product, Long brandId) {
        Brand brand = null;
        if (brandId != null) {
            brand = brandRepository.findById(brandId)
                    .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));
        }
        int updated = productRepository.updateProductDetails(
                id,
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                brand);
        if (updated == 0) throw new ResourceNotFoundException("Product", id);
        return "Product updated successfully.";
    }

    @Override
    public String deleteProduct(Long id) {
        if (!productRepository.existsById(id)) throw new ResourceNotFoundException("Product", id);
        productRepository.deleteById(id);
        return "Product deleted successfully.";
    }

    @Override
    public Product getProductByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Product", name));
    }

}
