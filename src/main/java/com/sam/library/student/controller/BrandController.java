package com.sam.library.student.controller;

import com.sam.library.student.common.ApiResponse;
import com.sam.library.student.common.PageResponse;
import com.sam.library.student.dto.BrandDTO;
import com.sam.library.student.dto.BrandFilterRequest;
import com.sam.library.student.mapper.BrandMapper;
import com.sam.library.student.service.BrandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/brand")
@Tag(name = "Brand", description = "Brand services APIs")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper brandMapper;

    @PreAuthorize("hasAuthority('BRAND_READ')")
    @GetMapping
    public ResponseEntity<PageResponse<BrandDTO>> getAllBrands(@ModelAttribute BrandFilterRequest filter) {
        Sort sort = filter.getSortOrder().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();
        Pageable pageable = PageRequest.of(filter.getPage() - 1, filter.getSize(), sort);
        Page<BrandDTO> result = brandService.getAllBrands(filter, pageable).map(brandMapper::toBrandDTO);
        return ResponseEntity.ok(PageResponse.of(result));
    }

    @PreAuthorize("hasAuthority('BRAND_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandDTO>> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(brandMapper.toBrandDTO(brandService.getBrandById(id))));
    }

    @PreAuthorize("hasAuthority('BRAND_CREATE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BrandDTO>> createBrand(@RequestBody BrandDTO dto) {
        return ResponseEntity.status(201).body(ApiResponse.success("Brand created",
                brandMapper.toBrandDTO(brandService.createBrand(brandMapper.toBrand(dto)))));
    }

    @PreAuthorize("hasAuthority('BRAND_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBrand(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(brandService.deleteBrand(id)));
    }

    @PreAuthorize("hasAuthority('BRAND_DELETE')")
    @DeleteMapping("/delete-all")
    public ResponseEntity<ApiResponse<String>> deleteBrands(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(ApiResponse.success(brandService.deleteBrands(ids)));
    }
}
