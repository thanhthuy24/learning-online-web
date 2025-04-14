package com.htt.elearning.course.response;

import com.htt.elearning.category.response.CategoryResponse;
import com.htt.elearning.tag.response.TagResponse;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestCourseResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Date createdDate;
    private Date updatedDate;
    private Float price;
    private Float discount;
    private String image;
    private CategoryResponse category;
    private TagResponse tag;
    private TeacherResponseClient teacher;
}
