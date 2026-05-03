package com.sam.library.student.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.library.student.dto.BrandDTO;
import com.sam.library.student.dto.BrandFilterRequest;
import com.sam.library.student.mapper.BrandMapper;
import com.sam.library.student.service.BrandService;
import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/brand")
@Tag(name = "Brand", description = "Brand services APIs")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @GetMapping
    public ResponseEntity<PageResponse<BrandDTO>> getAllBrands(@ModelAttribute BrandFilterRequest filter) {

        Sort sort = filter.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();

        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);
        Page<BrandDTO> result = brandService.getAllBrands(filter, pageable)
                .map(brandMapper::toBrandDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandDTO>> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(brandMapper.toBrandDTO(brandService.getBrandById(id))));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BrandDTO>> createBrand(@RequestBody BrandDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Brand created",
                brandMapper.toBrandDTO(brandService.createBrand(brandMapper.toBrand(dto)))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBrand(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(brandService.deleteBrand(id)));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<ApiResponse<String>> deleteBrands(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(ApiResponse.success(brandService.deleteBrands(ids)));
    }
}
