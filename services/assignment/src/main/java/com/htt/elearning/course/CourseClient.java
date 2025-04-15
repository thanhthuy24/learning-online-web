package com.htt.elearning.course;

import com.htt.elearning.course.response.CourseResponse;
import com.htt.elearning.course.response.TestCourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "course-service",
        contextId = "courseServiceV1",  // Thêm contextId duy nhất
        url = "${application.config.course-url}"
)
public interface CourseClient {
    @GetMapping("/get-courseId/{courseId}")
    CourseResponse getCourseByIdClient(
            @PathVariable Long courseId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/get-course-by-id/{courseId}")
    TestCourseResponse getFullCourseResponse(
            @PathVariable Long courseId
    );
}
