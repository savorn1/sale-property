package com.sam.library.student.controller;

import com.sam.library.student.dto.CreateProductDTO;
import com.sam.library.student.dto.ProductDTO;
import com.sam.library.student.entity.Product;
import com.sam.library.student.mapper.ProductMapper;
import com.sam.library.student.service.ProductService;
import com.sam.library.student.util.ApiResponse;
import com.sam.library.student.util.PageResponse;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/product")
@Tag(name = "Product", description = "Product services APIs")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<PageResponse<ProductDTO>> getAllProducts(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductDTO> result = productService.getAllProducts(pageable)
                .map(productMapper::toProductDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductDTO(product)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody CreateProductDTO dto) {
        Product created = productService.createProduct(productMapper.toProduct(dto), dto.getCategoryId(), dto.getBrandId());
        return ResponseEntity.status(201).body(ApiResponse.success("Product created", productMapper.toProductDTO(created)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable Long id, @RequestBody CreateProductDTO dto) {
        String result = productService.updateProduct(id, productMapper.toProduct(dto), dto.getBrandId());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        String result = productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductDTO(product)));
    }
}
