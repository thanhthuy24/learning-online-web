package com.htt.elearning.kafka;

import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.notification.dto.NotificationDTO;
import com.htt.elearning.notification.repository.NotificationRepository;
import com.htt.elearning.notification.service.NotificationService;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final EnrollmentClient enrollmentClient;
    private final HttpServletRequest request;

    @KafkaListener(topics = "lesson-topic",  groupId = "Notification-group")
    public void handleLessonCreatedEvent(LessonCreateEvent event, @Header("Authorization") String token) {
        log.info("📥 Received lesson created event: {}", event);
        if (token == null || token.isEmpty()) {
            log.error("No authorization token found in the request header");
            return; // Or throw an exception depending on your use case
        }
        try {
            List<UserResponse> users = enrollmentClient.getUsersByCourseIdClient(event.getCourseId(), token);
            users.forEach(user -> {
                NotificationDTO dto = NotificationDTO.builder()
                        .title("Khoá học " + event.getCourseName() + " bạn đang ký vừa có bài học mới!")
                        .message("Bài học mới: " + event.getName() + ", hãy check ngay nào, " + user.getUsername() + " ơi!")
                        .userId(user.getId())
                        .build();

                notificationService.createNotification(dto, token); // or send Firebase push
//                notificationService.sendNotificationToEnrolledUsers(event.getCourseId(), dto.getTitle(), dto.getMessage());
            });

        } catch (Exception e) {
            log.error("Error processing the assignment created event", e);
        }
    }

    @KafkaListener(topics = "assignment-topic", groupId = "Notification-group")
    public void handleAssignmentCreatedEvent(AssignmentCreateEvent event, @Header("Authorization") String token) {
        log.info("📥 Received assignment created event: {}", event);

        if (token == null || token.isEmpty()) {
            log.error("No authorization token found in the request header");
            return; // Or throw an exception depending on your use case
        }

        log.info("Authorization token: {}", token); // Log token to verify it's correct

        // Proceed with the request if token is valid
        try {
            List<UserResponse> users = enrollmentClient.getUsersByCourseIdClient(event.getCourseId(), token);
            log.info("Users found: {}", users);

            users.forEach(user -> {
                NotificationDTO dto = NotificationDTO.builder()
                        .title("Khoá học " + event.getCourseName() + " bạn đang ký vừa có bài tập mới!")
                        .message("Bài tập mới: " + event.getName() + ", hãy check ngay nào, " + user.getUsername() + " ơi!")
                        .userId(user.getId())
                        .build();
                notificationService.createNotification(dto, token);
            });
        } catch (Exception e) {
            log.error("Error processing the assignment created event", e);
        }
    }
}
