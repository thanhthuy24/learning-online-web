package com.htt.elearning.answerchoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerChoiceDTO {
    @JsonProperty("assignment_id")
    private Long assignmentId;
    @JsonProperty("question_id")
    private Long questionId;
    @JsonProperty("choice_id")
    private Long choiceId;
    @JsonProperty("user_id")
    private Long userId;
}
