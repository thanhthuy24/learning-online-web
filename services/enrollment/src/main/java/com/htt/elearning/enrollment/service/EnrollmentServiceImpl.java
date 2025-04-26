package com.htt.elearning.enrollment.service;

import com.htt.elearning.course.CourseClient;
import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.CourseResponseClient;
import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.enrollment.dtos.EnrollmentDTO;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import com.htt.elearning.enrollment.response.EnrollmentResponseClient;
import com.htt.elearning.progress.service.ProgressService;
import com.htt.elearning.teacher.response.TeacherResponseClient;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository ;
    private final CourseClient courseClient;
    private final UserClient userClient;
    private final ProgressService progressService;
    private final HttpServletRequest request;

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
    public List<EnrollmentResponseClient> getEnrollmentByUser() {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

        UserResponse currentUser = userClient.getUserByIdClient(userId, token);

        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);

        List<Long> courseIds = enrollments.stream()
                .map(Enrollment::getCourseId)
                .distinct()
                .collect(Collectors.toList());

        List<TestCourseResponse> courses = courseClient.getFullCourseResponses(courseIds);
        Map<Long, TestCourseResponse> coursesMap = courses.stream()
                .collect(Collectors.toMap(TestCourseResponse::getId, Function.identity()));

        List<EnrollmentResponseClient> enrollmentResponse = enrollments.stream()
                .map(enrollment -> {
                    TestCourseResponse course = coursesMap.get(enrollment.getCourseId());
                    return EnrollmentResponseClient.fromEnrollment(enrollment, course, currentUser);
                })
                .collect(Collectors.toList());
        return enrollmentResponse;
    }

    @Override
    public List<EnrollmentResponseClient> getCousesEnrolledByUser(Long userId) {
        String token = request.getHeader("Authorization");
        UserResponse currentUser = userClient.getUserByIdClient(userId, token);
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        List<Long> courseIds = enrollments.stream()
                .map(Enrollment::getCourseId)
                .distinct()
                .collect(Collectors.toList());

        List<TestCourseResponse> courses = courseClient.getFullCourseResponses(courseIds);
        Map<Long, TestCourseResponse> coursesMap = courses.stream()
                .collect(Collectors.toMap(TestCourseResponse::getId, Function.identity()));

        List<EnrollmentResponseClient> enrollmentResponse = enrollments.stream()
                .map(enrollment -> {
                    TestCourseResponse course = coursesMap.get(enrollment.getCourseId());
                    return EnrollmentResponseClient.fromEnrollment(enrollment, course, currentUser);
                })
                .collect(Collectors.toList());
        return enrollmentResponse;
    }

    @Override
    public Enrollment createEnrollment(EnrollmentDTO enrollmentDTO) {
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(enrollmentDTO.getCourseId(), token);
        if (existingCourse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find course with id "
                    + enrollmentDTO.getCourseId());
        }

        Long userId = userClient.getUserIdByUsername(token);

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

        progressService.createNewProgress(existingCourse.getId());

        return enrollment;
    }

    @Override
    public Long countEnrollmentByUserId(Long userId) {
        String token = request.getHeader("Authorization");
        Long role = userClient.getRoleIdClient(token);
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
        String token = request.getHeader("Authorization");
        CourseResponse existingCourse = courseClient.getCourseByIdClient(courseId, token);
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
        String token = request.getHeader("Authorization");
        List<Long> list = enrollmentRepository.findUsersByCourseId(courseId);
        List<UserResponse> userResponseList = new ArrayList<>();
        for (Long userId : list) {
            UserResponse u = userClient.getUserByIdClient(userId, token);
            userResponseList.add(u);
        }
        return userResponseList;
    }

}
