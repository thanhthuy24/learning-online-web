package com.htt.elearning.category.service;

import com.htt.elearning.category.dto.CategoryDTO;
import com.htt.elearning.category.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCateById(Long id);
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(Long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
