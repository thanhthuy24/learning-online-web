package com.htt.elearning.enrollment.response;

import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponseClient {
    private Long id;
    private Date enrollmentDate;
    private TestCourseResponse course;
    private UserResponse user;

    public static EnrollmentResponseClient fromEnrollment(Enrollment enrollment, TestCourseResponse testCourseResponse,
                                                    UserResponse userResponse) {
        EnrollmentResponseClient enrollmentResponse = EnrollmentResponseClient.builder()
                .id(enrollment.getId())
                .course(testCourseResponse)
                .user(userResponse)
                .enrollmentDate(enrollment.getEnrollmentDate())
                .build();
//        enrollmentResponse.setEnrollmentDate(new Date());
        return enrollmentResponse;
    }
}
