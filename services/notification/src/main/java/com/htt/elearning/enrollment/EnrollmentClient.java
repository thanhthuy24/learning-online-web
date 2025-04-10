package com.htt.elearning.enrollment;

import com.htt.elearning.user.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
            @RequestParam Long courseId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/get-userIds/{courseId}")
    List<Long> getUserIdsByCourseIdClient(
            @PathVariable Long courseId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/get-users/{courseId}")
    List<UserResponse> getUsersByCourseIdClient(
            @PathVariable Long courseId,
            @RequestHeader("Authorization") String token
    );
}
