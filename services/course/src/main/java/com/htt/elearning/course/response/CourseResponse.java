package com.htt.elearning.course.response;

import com.htt.elearning.category.response.CategoryResponse;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.tag.response.TagResponse;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Date createdDate;
    private Date updatedDate;
    private Float price;
    private Float discount;
    private String image;
    private Long categoryId;  // Dùng DTO thay vì entity
    private Long tagId;            // Dùng DTO thay vì entity
    private Long teacherId;

    public static CourseResponse fromCourse(Course course) {
        CourseResponse courseResponse = CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .description(course.getDescription())
                .isActive(course.getIsActive())
                .image(course.getImage())
                .categoryId(course.getCategory().getId())
                .teacherId(course.getTeacherId())
                .tagId(course.getTag().getId())
                .build();
        courseResponse.setCreatedDate(course.getCreatedDate());
        courseResponse.setUpdatedDate(course.getUpdatedDate());
        return courseResponse;
    }
}
