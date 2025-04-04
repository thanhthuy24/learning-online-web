package com.htt.elearning.notification.dto;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {
    private String to;
    private String username;
    private String password;
}
