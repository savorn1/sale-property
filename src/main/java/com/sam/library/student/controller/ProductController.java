package com.sam.library.student.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.common.UserContext;
import com.sam.library.student.dto.CreateProductDTO;
import com.sam.library.student.dto.ProductDTO;
import com.sam.library.student.entity.Product;
import com.sam.library.student.mapper.ProductMapper;
import com.sam.library.student.service.ProductService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/product")
@Tag(name = "Product", description = "Product services APIs")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<ProductDTO>> getAllProducts(
            @Parameter(description = "Page number, 1-based", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Filter by name (partial match)")
            @RequestParam(required = false) String name) {

        Long userId = UserContext.getUserId();
        log.info("getAllProducts called by userId={}", userId);

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ProductDTO> result = productService.getAllProducts(name, pageable)
                .map(productMapper::toProductDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductDTO(product)));
    }

    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@RequestBody CreateProductDTO dto) {
        Product created = productService.createProduct(productMapper.toProduct(dto), dto.getCategoryId(), dto.getBrandId());
        return ResponseEntity.status(201).body(ApiResponse.success(productMapper.toProductDTO(created)));
    }

    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updateProduct(@PathVariable Long id, @RequestBody CreateProductDTO dto) {
        String result = productService.updateProduct(id, productMapper.toProduct(dto), dto.getBrandId());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        String result = productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PreAuthorize("hasAuthority('PRODUCT_READ')")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductDTO(product)));
    }

}