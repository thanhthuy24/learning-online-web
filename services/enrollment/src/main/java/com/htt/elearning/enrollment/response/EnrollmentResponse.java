package com.htt.elearning.enrollment.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponse {
    private Long id;
    private Date enrollmentDate;
    private Long courseId;
    private Long userId;
}
