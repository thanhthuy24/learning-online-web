package com.htt.elearning.course.response;

import com.htt.elearning.course.pojo.Course;
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
    private Long categoryId;
    private Long tagId;
    private Long teacherId;

    private int totalPages;

    public static CourseResponseRedis fromCourse(Course course) {
        CourseResponseRedis courseResponseRedis = CourseResponseRedis.builder()
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
                .totalPages(0)
                .build();
        courseResponseRedis.setCreatedDate(course.getCreatedDate());
        courseResponseRedis.setUpdatedDate(course.getUpdatedDate());
        return courseResponseRedis;
    }
}
