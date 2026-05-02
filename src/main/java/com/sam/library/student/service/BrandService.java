package com.sam.library.student.service;

import java.util.List;

import com.sam.library.student.entity.Brand;

public interface  BrandService {
    List<Brand> getAllBrands();
    Brand getBrandById(Long id);
    Brand createBrand(Brand brand);
    Brand updateBrand(Long id, Brand brand);
    String deleteBrand(Long id);
    String deleteBrands(List<Long> ids);
    
}
