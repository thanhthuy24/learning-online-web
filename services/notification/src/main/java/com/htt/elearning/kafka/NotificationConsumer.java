package com.htt.elearning.kafka;

import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.notification.dto.NotificationDTO;
import com.htt.elearning.notification.repository.NotificationRepository;
import com.htt.elearning.notification.service.NotificationService;
import com.htt.elearning.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final EnrollmentClient enrollmentClient;

    @KafkaListener(topics = "lesson-topic",  groupId = "Notification-group")
    public void handleLessonCreatedEvent(LessonCreateEvent event) {
        log.info("📥 Received lesson created event: {}", event);

        List<UserResponse> users = enrollmentClient.getUsersByCourseIdClient(event.getCourseId());

        users.forEach(user -> {
            NotificationDTO dto = NotificationDTO.builder()
                    .title("Khoá học " + event.getCourseName() + " bạn đang ký vừa có bài học mới!")
                    .message("Bài học mới: " + event.getName() + ", hãy check ngay nào, " + user.getUsername() + " ơi!")
                    .userId(user.getId())
                    .build();

            notificationService.createNotification(dto); // or send Firebase push
        });
    }

    @KafkaListener(topics = "assignment-topic",  groupId = "Notification-group")
    public void handleAssignmentCreatedEvent(AssignmentCreateEvent event) {
        log.info("📥 Received assignment created event: {}", event);

        List<UserResponse> users = enrollmentClient.getUsersByCourseIdClient(event.getCourseId());

        users.forEach(user -> {
            NotificationDTO dto = NotificationDTO.builder()
                    .title("Khoá học " + event.getCourseName() + " bạn đang ký vừa có bài tập mới!")
                    .message("Bài tập mới: " + event.getName() + ", hãy check ngay nào, " + user.getUsername() + " ơi!")
                    .userId(user.getId())
                    .build();

            notificationService.createNotification(dto); // or send Firebase push
        });
    }
}
