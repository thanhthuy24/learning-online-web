package com.htt.elearning.notification.response;

import com.htt.elearning.notification.pojo.Notification;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationListResponse {
    private List<Notification> notifications;
    private int totalPages;
}
