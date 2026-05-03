package com.sam.library.student.controller;

import com.sam.library.student.dto.SupplierDTO;
import com.sam.library.student.dto.SupplierFilterRequest;
import com.sam.library.student.mapper.SupplierMapper;
import com.sam.library.student.service.SupplierService;
import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/supplier")
@Tag(name = "Supplier", description = "Supplier management APIs")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final SupplierMapper supplierMapper;

    @GetMapping
    public ResponseEntity<PageResponse<SupplierDTO>> getAllSuppliers(@ModelAttribute SupplierFilterRequest filter) {
        Sort sort = filter.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);
        Page<SupplierDTO> result = supplierService.getAllSuppliers(filter, pageable)
                .map(supplierMapper::toSupplierDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDTO>> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(supplierMapper.toSupplierDTO(supplierService.getSupplierById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupplierDTO>> createSupplier(@RequestBody SupplierDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Supplier created",
                supplierMapper.toSupplierDTO(supplierService.createSupplier(supplierMapper.toSupplier(dto)))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SupplierDTO>> updateSupplier(@PathVariable Long id, @RequestBody SupplierDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Supplier updated",
                supplierMapper.toSupplierDTO(supplierService.updateSupplier(id, supplierMapper.toSupplier(dto)))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSupplier(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.deleteSupplier(id)));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ApiResponse<String>> deleteSuppliers(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.deleteSuppliers(ids)));
    }
}
