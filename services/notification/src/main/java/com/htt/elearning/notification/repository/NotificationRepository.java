package com.htt.elearning.notification.repository;

import com.htt.elearning.notification.pojo.Notification;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findNotificationsByUserId(Long userId, Pageable pageable);
    Notification findNotificationById(Long id);
}
