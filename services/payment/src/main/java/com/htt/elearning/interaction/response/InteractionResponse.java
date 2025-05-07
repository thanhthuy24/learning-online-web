package com.htt.elearning.interaction.response;

import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InteractionResponse {
    private Long id;
    private UserResponse user;
    private TestCourseResponse course;
    private Long clicks;
    private Boolean purchased;
}
