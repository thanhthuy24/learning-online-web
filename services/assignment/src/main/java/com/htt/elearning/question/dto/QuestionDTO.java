package com.htt.elearning.question.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionDTO {
    @NotNull
    private String content;
    @JsonProperty("assignment_id")
    private Long assignmentId;
}
