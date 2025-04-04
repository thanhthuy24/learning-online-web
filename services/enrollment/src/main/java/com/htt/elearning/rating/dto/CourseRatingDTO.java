package com.htt.elearning.rating.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRatingDTO {
    @NotNull
    private Long rating;
    @Size(min = 1, max = 255, message = "Comment is required!!")
    private String comment;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("course_id")
    private Long courseId;
}
