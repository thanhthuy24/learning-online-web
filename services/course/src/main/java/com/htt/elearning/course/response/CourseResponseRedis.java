package com.htt.elearning.course.response;

import com.htt.elearning.category.pojo.Category;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.tag.pojo.Tag;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponseRedis {
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private Date createdDate;
    private Date updatedDate;
    private Float price;
    private Float discount;
    private String image;
    private Category category;
    private Tag tag;
    private TeacherResponseClient teacher;

    private int totalPages;

    public static CourseResponseRedis fromCourse(Course course, TeacherResponseClient teacherResponseClient) {
        CourseResponseRedis courseResponse = CourseResponseRedis.builder()
                .id(course.getId())
                .name(course.getName())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .description(course.getDescription())
                .isActive(course.getIsActive())
                .image(course.getImage())
                .category(course.getCategory())
                .teacher(teacherResponseClient)
                .tag(course.getTag())
                .build();
        courseResponse.setCreatedDate(course.getCreatedDate());
        courseResponse.setUpdatedDate(course.getUpdatedDate());
        return courseResponse;
    }
}
