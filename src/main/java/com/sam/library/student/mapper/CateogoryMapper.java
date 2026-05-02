package com.sam.library.student.mapper;

import org.springframework.stereotype.Component;
import com.sam.library.student.dto.CategoryDTO;
import com.sam.library.student.entity.Category;

@Component
public class CateogoryMapper {

    public CategoryDTO toCategoryDTO(Category b) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(b.getId());
        dto.setName(b.getName());
        dto.setDescription(b.getDescription());
        return dto;
    }

    public Category toCategory(CategoryDTO dto) {
        Category brand = new Category();
        brand.setName(dto.getName());
        return brand;
    }
}
