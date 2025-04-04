package com.htt.elearning.notification;

import com.htt.elearning.notification.dto.NotificationDTO;
import com.htt.elearning.notification.response.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notification-service",
        url = "${application.config.notification-url}"
)
public interface NotificationClient {
    @PostMapping("")
    NotificationResponse createNotification(
            @RequestBody NotificationDTO notificationDTO
    );
}
