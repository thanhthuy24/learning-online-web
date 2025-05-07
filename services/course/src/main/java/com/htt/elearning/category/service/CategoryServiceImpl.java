package com.htt.elearning.category.service;

import com.htt.elearning.category.dto.CategoryDTO;
import com.htt.elearning.category.pojo.Category;
import com.htt.elearning.category.repository.CategoryRepository;
import com.htt.elearning.category.response.CategoryResponse;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryResponse convertToPojo(Category category) {
        if (category == null) {
            return null;
        }
        return modelMapper.map(category, CategoryResponse.class);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> convertToPojo(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCateById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> convertToPojo(category))
                .orElse(null);
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
        existingCategory.setName(categoryDTO.getName());
        Category updateCategory = categoryRepository.save(existingCategory);

        return modelMapper.map(updateCategory, CategoryResponse.class);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryResponse> getListCategories(List<Long> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> {
                    return CategoryResponse.fromCategory(category);
                })
                .collect(Collectors.toList());
        return categoryResponses;
    }
}
