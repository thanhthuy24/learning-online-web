package com.htt.elearning.category;

import com.htt.elearning.category.response.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(
        name = "course-service",
        url = "${application.config.category-url}"
)
public interface CategoryClient {
    @GetMapping("/get-list-categories-by-ids")
    List<CategoryResponse> getAllCategoriesByIds(
            @RequestParam List<Long> ids
    );
}
