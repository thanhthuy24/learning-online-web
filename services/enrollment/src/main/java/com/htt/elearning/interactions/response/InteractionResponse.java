package com.htt.elearning.interactions.response;

import com.htt.elearning.course.response.TestCourseResponse;
import com.htt.elearning.interactions.pojo.Interaction;
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

    public static InteractionResponse fromInteraction(Interaction interaction, UserResponse userResponse,
                                                      TestCourseResponse testCourseResponse) {
        InteractionResponse interactionResponse = InteractionResponse.builder()
                .id(interaction.getId())
                .user(userResponse)
                .course(testCourseResponse)
                .clicks(interaction.getClicks())
                .purchased(interaction.getPurchased())
                .build();
        return interactionResponse;
    }
}
