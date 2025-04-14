package com.htt.elearning.progress;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "enrollment-service",
        contextId = "enrollmentServiceV2",  // Thêm contextId duy nhất
        url = "${application.config.progress-url}"
)
public interface ProgressClient {
    @PostMapping("/course/{courseId}")
    Float createProgress(
            @PathVariable Long courseId,
            @RequestHeader("Authorization") String token
    );
}
