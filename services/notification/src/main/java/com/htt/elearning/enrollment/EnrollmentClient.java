package com.htt.elearning.enrollment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "enrollment-service",
        url = "${application.config.enrollment-url}"
)
public interface EnrollmentClient {
    @GetMapping("check-enrollment-boolean")
    Boolean checkEnrollment(
            @RequestParam Long userId,
            @RequestParam Long courseId
    );

    @GetMapping("/get-users/{courseId}")
    List<Long> getUsersByCourseIdClient(
            @PathVariable Long courseId
    );
}
