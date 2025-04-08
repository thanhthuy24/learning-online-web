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
public class CourseResponseRedisList {
    private List<CourseResponseRedis> courses;
    private int totalPages;
}
