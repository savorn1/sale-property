package com.sam.library.student.service;

import java.util.List;

import com.sam.library.student.entity.Category;

public interface CategoryService {
        List<Category> getAllCategories();
        Category getCategoryById(Long id);
        Category createCategory(Category category);
        Category updateCategory(Long id, Category category);
        String deleteCategory(Long id);
        String deleteCategories(List<Long> ids);

}
