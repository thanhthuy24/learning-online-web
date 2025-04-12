package com.htt.elearning.enrollment;

import com.htt.elearning.enrollment.dto.EnrollmentDTO;
import com.htt.elearning.enrollment.response.EnrollmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "enrollment-service",
        url = "${application.config.enrollment-url}"
)
public interface EnrollmentClient {
    @GetMapping("check-enrollment-boolean")
    Boolean checkEnrollment(
            @RequestParam Long userId,
            @RequestParam Long courseId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/get-users/{courseId}")
    List<Long> getUsersByCourseIdClient(
            @PathVariable Long courseId
    );

    @PostMapping("/create-enroll")
    EnrollmentResponse createEnrollmentClient(
            @RequestBody EnrollmentDTO enrollmentDTO,
            @RequestHeader("Authorization") String token
    );
}
