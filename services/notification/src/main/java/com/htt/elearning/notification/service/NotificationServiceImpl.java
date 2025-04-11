package com.htt.elearning.notification.service;

import com.htt.elearning.enrollment.EnrollmentClient;
import com.htt.elearning.configs.FirebaseMessagingService;
import com.htt.elearning.notification.dto.NotificationDTO;
import com.htt.elearning.notification.pojo.Notification;
import com.htt.elearning.notification.repository.NotificationRepository;
import com.htt.elearning.notification.response.NotificationResponse;
import com.htt.elearning.token.TokenClient;
import com.htt.elearning.token.response.TokenResponse;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final EnrollmentClient enrollmentClient;
    private final TokenClient tokenClient;
    private final FirebaseMessagingService firebaseMessagingService;
    private final NotificationRepository notificationRepository;
    private final UserClient userClient;
    private final ModelMapper modelMapper;
    private final HttpServletRequest request;

    @Override
    public void sendNotificationToEnrolledUsers(Long courseId, String title, String body) throws Exception {
        String token = request.getHeader("Authorization");
        List<Long> enrolledUserIds = enrollmentClient.getUserIdsByCourseIdClient(courseId, token);
        List<TokenResponse> tokens = tokenClient.getListTokens(enrolledUserIds);

        for (TokenResponse fcmToken : tokens) {
            firebaseMessagingService.sendNotification(fcmToken.getToken(), title, body);
        }
    }

    @Override
    public Page<Notification> findNotificationsByUserId(Pageable pageable) {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);
        return notificationRepository.findNotificationsByUserId(userId, pageable);
    }

    @Override
    public NotificationResponse createNotification(NotificationDTO notificationDTO, String token) {
//        String token = request.getHeader("Authorization");
        UserResponse existingUser = userClient.getUserByIdClient(notificationDTO.getUserId(), token);
        Notification newNotification = Notification.builder()
                .title(notificationDTO.getTitle())
                .message(notificationDTO.getMessage())
                .userId(existingUser.getId())
                .isRead(false)
                .createdDate(new Date())
                .build();

        notificationRepository.save(newNotification);

        return modelMapper.map(newNotification, NotificationResponse.class);
    }

    @Override
    public Notification updateNotification(Long notificationId) {
        Notification existingNotification = notificationRepository.findNotificationById(notificationId);
        existingNotification.setIsRead(true);
        notificationRepository.save(existingNotification);
        return existingNotification;
    }
}
