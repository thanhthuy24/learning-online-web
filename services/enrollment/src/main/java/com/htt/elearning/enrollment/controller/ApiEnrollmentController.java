package com.htt.elearning.enrollment.controller;

import com.htt.elearning.enrollment.dtos.EnrollmentDTO;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import com.htt.elearning.enrollment.response.EnrollmentResponse;
import com.htt.elearning.enrollment.service.EnrollmentService;
import com.htt.elearning.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/enrollments")
public class ApiEnrollmentController {
    private final EnrollmentService enrollmentService;
    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createEnrollment(
            @RequestBody EnrollmentDTO enrollmentDTO,
            BindingResult rs
    ){
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        enrollmentService.createEnrollment(enrollmentDTO);
        return ResponseEntity.ok(enrollmentDTO);
    }

    @GetMapping("check-enrollment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getEnrollmentByUserAndCourse(
            @RequestParam Long userId,
            @RequestParam Long courseId
    ){
        Optional<Enrollment> enrollment = enrollmentService.findByUserIdAndCourseId(userId, courseId);
        return enrollment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("check-enrollment-boolean")
    @ResponseStatus(HttpStatus.OK)
    public Boolean checkEnrollment(
            @RequestParam Long userId,
            @RequestParam Long courseId
    ){
        return enrollmentService.checkEnrolled(userId, courseId);
    }

    @GetMapping("/get-courses")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Enrollment>> getEnrollments(
    ){
        List<Enrollment> enrollments = enrollmentService.getEnrollmentByUser();
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/get-courses/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Enrollment>> getEnrollments(
            @PathVariable Long userId
    ){
        List<Enrollment> enrollments = enrollmentService.getCousesEnrolledByUser(userId);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/course/{courseId}/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> getEnrollmentCountByCourse(
            @PathVariable Long courseId
    ){
        return ResponseEntity.ok(enrollmentService.getCountEnrollmentByCourseId(courseId));
    }

    @GetMapping("/count-enroll/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> countEnrollmentByUser(
            @PathVariable Long userId
    ){
        return ResponseEntity.ok(enrollmentService.countEnrollmentByUserId(userId));
    }

    @GetMapping("/get-users/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUsersByCourseIdClient(
            @PathVariable Long courseId
    ){
        return enrollmentService.getUsersByCourseIdClient(courseId);
    }

    @PostMapping("/create-enroll")
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResponse createEnrollmentClient(
            @RequestBody EnrollmentDTO enrollmentDTO
    ){
        Enrollment newEnrollment = enrollmentService.createEnrollment(enrollmentDTO);
        return modelMapper.map(newEnrollment, EnrollmentResponse.class);
    }

}
