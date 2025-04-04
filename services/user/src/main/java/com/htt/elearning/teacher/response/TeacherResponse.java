package com.htt.elearning.teacher.response;

import com.htt.elearning.user.response.UserResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private Long id;
    private String position;
    private String description;
    private UserResponse user;
}
