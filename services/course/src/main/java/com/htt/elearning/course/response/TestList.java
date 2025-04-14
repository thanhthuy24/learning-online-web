package com.htt.elearning.course.response;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestList {
    private List<TestCourseResponse> courses;
    private int totalPages;
}
