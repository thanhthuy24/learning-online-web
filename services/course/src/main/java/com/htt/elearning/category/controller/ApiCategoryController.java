package com.htt.elearning.category.controller;

import com.htt.elearning.category.dto.CategoryDTO;
import com.htt.elearning.category.pojo.Category;
import com.htt.elearning.category.response.CategoryResponse;
import com.htt.elearning.category.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/category")
public class ApiCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CategoryResponse>> getAllCategories(
    ) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult rs)
    {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("insert category successfully!");
    }

    @PutMapping("/{cateId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> updateCategory(
            @PathVariable Long cateId,
            @Valid @RequestBody CategoryDTO categoryDTO)
    {
        categoryService.updateCategory(cateId, categoryDTO);
        return ResponseEntity.ok("update category successfully!");
    }

    @DeleteMapping("/{cateId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteCategory(@PathVariable Long cateId) {
        categoryService.deleteCategory(cateId);
        return ResponseEntity.ok("delete category successfully!");
    }

    @GetMapping("/get-list-categories-by-ids")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getAllCategoriesByIds(
            @RequestParam List<Long> ids
    ){
        return categoryService.getListCategories(ids);
    }

}
