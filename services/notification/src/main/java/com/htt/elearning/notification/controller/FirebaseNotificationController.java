package com.htt.elearning.notification.controller;

import com.htt.elearning.configs.FirebaseMessagingService;
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

    @PostMapping("/send")
    public String sendNotification(
            @RequestParam String token,
            @RequestParam String title,
            @RequestParam String body) {
        try {
            return firebaseService.sendNotification(token, title, body);
        } catch (Exception e) {
            return "Failed to send notification: " + e.getMessage();
        }
    }
}
