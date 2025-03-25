package com.htt.elearning.enrollment.service;

import com.htt.elearning.enrollment.dtos.EnrollmentDTO;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository ;
//    private final CourseRepository courseRepository;
//    private final UserRepository userRepository;
//    private final ProgressService progressService;

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
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = userRepository.getUserByUsername(username);
//
//        List<Enrollment> enrollments = enrollmentRepository.findByUserId(user.getId());
//        return enrollments;
        return null;
    }
    @Override
    public List<Enrollment> getCousesEnrolledByUser(Long userId) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long role = userRepository.getUserByUsername(username).getRole().getId();
//        if (role != 2){
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN, "Authorization!!"
//            );
//        }
//        return enrollmentRepository.findByUserId(userId);
        return null;
    }

    @Override
    public Enrollment createEnrollment(EnrollmentDTO enrollmentDTO) {
//        Course existingCourse = courseRepository.findById(enrollmentDTO.getCourseId())
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Cannot find course with id " + enrollmentDTO.getCourseId()));
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user1 = userRepository.getUserByUsername(username);
//
//        Optional<Enrollment> enrollments = enrollmentRepository.findByUserIdAndCourseId(user1.getId(), enrollmentDTO.getCourseId());
//        if (!enrollments.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This course had in your list course!!");
//        }
//
//        Enrollment enrollment = Enrollment.builder()
//                .course(existingCourse)
//                .user(user1)
//                .build();
//        enrollmentRepository.save(enrollment);
//
//        progressService.calculateProgress(existingCourse.getId());
//
//        return enrollment;
        return null;
    }

    @Override
    public Long countEnrollmentByUserId(Long userId) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long role = userRepository.getUserByUsername(username).getRole().getId();
//        if (role != 2){
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN, "Authorization!!"
//            );
//        }
//
//        return enrollmentRepository.countEnrollmentByUserId(userId);
        return null;
    }

//    @Override
//    public List<User> getEnrolledUsers(Long courseId) {
//        List<Enrollment> enrollments = enrollmentRepository.findAllByCourseId(courseId);
//        return enrollments.stream().map(Enrollment::getUser).collect(Collectors.toList());
//    }

//    @Override
//    public List<Long> getEnrolledUserIds(Long courseId) {
//        return enrollmentRepository.findAllEnrollmentByCourseId(courseId);
//    }

    @Override
    public Long getCountEnrollmentByCourseId(Long courseId) {
//        Course existingCourse = courseRepository.findById(courseId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Cannot find course with id " + courseId));
//
//        Long count = enrollmentRepository.countEnrollmentByCourseId(existingCourse.getId());
//        if (count != null) {
//            return count;
//        }
        return 0L;
    }
}
