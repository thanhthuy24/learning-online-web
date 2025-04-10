package com.htt.elearning.notification.service;

import com.htt.elearning.notification.dto.NotificationDTO;
import com.htt.elearning.notification.pojo.Notification;
import com.htt.elearning.notification.response.NotificationResponse;
import org.springframework.data.domain.*;

public interface NotificationService {
    void sendNotificationToEnrolledUsers(Long courseId, String title, String body) throws Exception;

    Page<Notification> findNotificationsByUserId(Pageable pageable);
    NotificationResponse createNotification(NotificationDTO notificationDTO, String token);
    //    Notification createNotification(String title, String message, Long userId);
    Notification updateNotification(Long notificationId);
}
