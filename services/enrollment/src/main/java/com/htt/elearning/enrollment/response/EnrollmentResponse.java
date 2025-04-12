package com.htt.elearning.enrollment.response;

import com.htt.elearning.enrollment.pojo.Enrollment;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentResponse {
    private Long id;
    private Date enrollmentDate;
    private Long courseId;
    private Long userId;

    public static EnrollmentResponse fromEnrollment(Enrollment enrollment) {
        EnrollmentResponse enrollmentResponse = EnrollmentResponse.builder()
                .id(enrollment.getId())
                .courseId(enrollment.getCourseId())
                .userId(enrollment.getUserId())
                .build();
        enrollmentResponse.setEnrollmentDate(new Date());
        return enrollmentResponse;
    }
}
