package com.htt.elearning.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "notification-service",
        contextId = "notificationServiceV2",
        url = "${application.config.firebase-url}"
)
public interface FirebaseClient {
    @PostMapping("/send")
    void sendNotification(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam String body,
            @RequestHeader("Authorization") String token
    ) throws Exception;
}
