package com.htt.elearning.notification.controller;

import com.htt.elearning.configs.FirebaseMessagingService;
import com.htt.elearning.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class FirebaseNotificationController {
    private final FirebaseMessagingService firebaseService;
    private final NotificationService notificationService;

    @PostMapping("/send")
    public void sendNotification(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam String body) throws Exception {
            notificationService.sendNotificationToEnrolledUsers(courseId, title, body);
    }
}
