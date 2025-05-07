package com.htt.elearning.category.response;

import com.htt.elearning.category.pojo.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;

    public static CategoryResponse fromCategory(Category category) {
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
        return categoryResponse;
    }
}
