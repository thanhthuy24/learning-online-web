package com.htt.elearning.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String title;
    private String message;
    @JsonProperty("user_id")
    private Long userId;
}
