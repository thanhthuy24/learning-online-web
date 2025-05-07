package com.htt.elearning.interaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InteractionDTO {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("course_id")
    private Long courseId;
    private Long clicks;
    private Boolean purchased;
}
