package com.sam.library.student.mapper;

import org.springframework.stereotype.Component;

import com.sam.library.student.dto.BrandDTO;
import com.sam.library.student.entity.Brand;

@Component
public class BrandMapper {

    public BrandDTO toBrandDTO(Brand b) {
        BrandDTO dto = new BrandDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        return dto;
    }

    public Brand toBrand(BrandDTO dto) {
        Brand brand = new Brand();
        brand.setName(dto.getName());
        return brand;
    }
}
