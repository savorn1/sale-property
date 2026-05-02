package com.sam.library.student.controller;

import com.sam.library.student.dto.ProductDTO;
import com.sam.library.student.entity.Product;
import com.sam.library.student.mapper.ProductMapper;
import com.sam.library.student.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.sam.library.student.dto.CreateProductDTO;

@RestController
@RequestMapping("api/product")
@Tag(name = "Product", description = "Product services APIs")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts()
                .stream()
                .map(productMapper::toProductDTO)
                .toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productMapper.toProductDTO(product));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductDTO dto) {
        Product created = productService.createProduct(productMapper.toProduct(dto), dto.getCategoryId(), dto.getBrandId());
        return ResponseEntity.status(201).body(productMapper.toProductDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody CreateProductDTO dto) {
        String result = productService.updateProduct(id, productMapper.toProduct(dto), dto.getBrandId());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String result = productService.deleteProduct(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.ok(productMapper.toProductDTO(product));
    }
}
