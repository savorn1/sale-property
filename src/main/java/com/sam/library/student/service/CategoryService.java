package com.sam.library.student.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sam.library.student.entity.Category;

public interface CategoryService {
        List<Category> getAllCategories();
        Page<Category> getAllCategories(Pageable pageable);
        Category getCategoryById(Long id);
        Category createCategory(Category category);
        Category updateCategory(Long id, Category category);
        String deleteCategory(Long id);
        String deleteCategories(List<Long> ids);

}
