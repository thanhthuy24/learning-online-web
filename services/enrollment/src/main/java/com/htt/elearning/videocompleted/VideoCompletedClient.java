package com.htt.elearning.videocompleted;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "course-service",
        contextId = "courseServiceV4",
        url = "${application.config.video-completed-url}"
)
public interface VideoCompletedClient {
    @GetMapping("/count-video-completed/{lessonId}")
    Long countVideoCompletedBy (
            @PathVariable Long lessonId,
            @RequestHeader("Authorization") String token
    );
}
