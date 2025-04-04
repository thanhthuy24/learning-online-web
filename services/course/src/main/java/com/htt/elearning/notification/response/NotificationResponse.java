package com.htt.elearning.notification.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private Date createdDate;
    private String title;
    private String message;
    private Long userId;
    private Boolean isRead;
}
