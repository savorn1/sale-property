package com.sam.library.student.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sam.library.student.entity.Category;
import com.sam.library.student.repository.CategoryRepository;
import com.sam.library.student.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category createCategory(Category category) {
        String name = category.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        if (categoryRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Category with name '" + name + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepository.findById(id).orElse(null);
        if (existing == null) {
            throw new IllegalArgumentException("Category with id '" + id + "' not found.");
        }
        String name = category.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be null or empty.");
        }
        if (categoryRepository.findByName(name).isPresent() && !existing.getName().equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("Category with name '" + name + "' already exists.");
        }
        existing.setName(name);
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    @Override
    public String deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Category with id '" + id + "' not found.");
        }
        categoryRepository.deleteById(id);
        return "Category with id '" + id + "' has been deleted successfully.";
    }

    @Override
    public String deleteCategories(List<Long> ids) {
        List<Category> toDelete = categoryRepository.findAllById(ids);
        if (toDelete.isEmpty()) {
            return "No categories found for the provided IDs.";
        }
        categoryRepository.deleteAll(toDelete);
        return "Deleted " + toDelete.size() + " categories successfully.";
    }
}
