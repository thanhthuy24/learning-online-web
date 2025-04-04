package com.htt.elearning.essay.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EssayDTO {
    @NotNull
    @Size(min = 2, max = 4000, message = "Content must be between 2 characters and 4000 characters")
    private String content;
    @JsonProperty("assignment_id")
    private Long assignmentId;
    @JsonProperty("question_id")
    private Long questionId;
    @JsonProperty("user_id")
    private Long userId;
}