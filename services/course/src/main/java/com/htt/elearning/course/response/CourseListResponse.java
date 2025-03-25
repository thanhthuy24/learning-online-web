package com.htt.elearning.course.response;

import com.htt.elearning.course.pojo.Course;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseListResponse {
    private List<Course> courses;
    private int totalPages;
}
