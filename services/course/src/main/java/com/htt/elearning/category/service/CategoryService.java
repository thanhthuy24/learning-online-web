package com.htt.elearning.category.service;

import com.htt.elearning.category.dto.CategoryDTO;
import com.htt.elearning.category.pojo.Category;
import com.htt.elearning.category.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCateById(Long id);
    Category createCategory(CategoryDTO categoryDTO);
    CategoryResponse updateCategory(Long categoryId, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
