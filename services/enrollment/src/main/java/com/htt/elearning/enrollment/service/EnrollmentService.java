package com.htt.elearning.enrollment.service;

import com.htt.elearning.enrollment.dtos.EnrollmentDTO;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.response.EnrollmentResponseClient;
import com.htt.elearning.user.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    Boolean checkEnrolled(Long userId, Long courseId);
    Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId);
    List<EnrollmentResponseClient> getEnrollmentByUser();
    List<EnrollmentResponseClient> getCousesEnrolledByUser(Long userId);
    Long getCountEnrollmentByCourseId(Long courseId);
    Enrollment createEnrollment(EnrollmentDTO enrollmentDTO);

    //    đếm số lượng course mà user đã đăng ký
    Long countEnrollmentByUserId(Long userId);

//    List<User> getEnrolledUsers(Long courseId);
    List<Long> getEnrolledUserIds(Long courseId);

//    enrollment - client
    List<UserResponse> getUsersByCourseIdClient(Long courseId);
}
