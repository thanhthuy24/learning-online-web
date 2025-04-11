package com.htt.elearning.video;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "course-service",
        contextId = "courseServiceV3",
        url = "${application.config.video-url}"
)
public interface VideoClient {
    @GetMapping("/count/lesson/{lessonId}")
    Long getLessonCountClient(
            @PathVariable Long lessonId,
            @RequestHeader("Authorization") String token
    );
}
