package com.htt.elearning.enrollment.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.enrollment.dtos.EnrollmentDTO;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import com.htt.elearning.progress.service.ProgressService;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository ;
    private final CourseClient courseClient;
    private final UserClient userClient;
    private final ProgressService progressService;

    @Override
    public Boolean checkEnrolled(Long userId, Long courseId) {
        Optional<Enrollment> listEnrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
        if (listEnrollment.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public Optional<Enrollment> findByUserIdAndCourseId(Long userId, Long courseId) {
        return enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public List<Enrollment> getEnrollmentByUser() {
        Long userId = userClient.getUserIdByUsername();
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        return enrollments;
    }

    @Override
    public List<Enrollment> getCousesEnrolledByUser(Long userId) {
        Long role = userClient.getRoleIdClient();
        if (role != 2){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Authorization!!"
            );
        }
        return enrollmentRepository.findByUserId(userId);
    }

    @Override
    public Enrollment createEnrollment(EnrollmentDTO enrollmentDTO) {
        CourseResponse existingCourse = courseClient.getCourseByIdClient(enrollmentDTO.getCourseId());
        if (existingCourse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find course with id "
                    + enrollmentDTO.getCourseId());
        }
        Long userId = userClient.getUserIdByUsername();

        Optional<Enrollment> enrollments = enrollmentRepository.findByUserIdAndCourseId(
                userId, enrollmentDTO.getCourseId());
        if (!enrollments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This course had in your list course!!");
        }

        Enrollment enrollment = Enrollment.builder()
                .courseId(existingCourse.getId())
                .userId(userId)
                .build();
        enrollmentRepository.save(enrollment);

        progressService.calculateProgress(existingCourse.getId());

        return enrollment;
    }

    @Override
    public Long countEnrollmentByUserId(Long userId) {
        Long role = userClient.getRoleIdClient();
        if (role != 2){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Authorization!!"
            );
        }

        return enrollmentRepository.countEnrollmentByUserId(userId);
    }

//    @Override
//    public List<User> getEnrolledUsers(Long courseId) {
//        List<Enrollment> enrollments = enrollmentRepository.findAllByCourseId(courseId);
//        return enrollments.stream().map(Enrollment::getUser).collect(Collectors.toList());
//    }

    @Override
    public List<Long> getEnrolledUserIds(Long courseId) {
        return enrollmentRepository.findAllEnrollmentByCourseId(courseId);
    }

    @Override
    public Long getCountEnrollmentByCourseId(Long courseId) {
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId);
        if (existingCourse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find course with id "
                    + courseId);
        }

        Long count = enrollmentRepository.countEnrollmentByCourseId(existingCourse.getId());
        if (count != null) {
            return count;
        }
        return 0L;
    }

//    enrollment - client
    @Override
    public List<UserResponse> getUsersByCourseIdClient(Long courseId) {
        List<Long> list = enrollmentRepository.findUsersByCourseId(courseId);
        List<UserResponse> userResponseList = new ArrayList<>();
        for (Long userId : list) {
            UserResponse u = userClient.getUserByIdClient(userId);
            userResponseList.add(u);
        }
        return userResponseList;
    }

}
