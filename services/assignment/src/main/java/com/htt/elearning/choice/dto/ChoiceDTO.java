package com.htt.elearning.choice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChoiceDTO {
    @NotNull
    private String content;
    @JsonProperty("is_correct")
    private boolean isCorrect;
    @JsonProperty("question_id")
    private Long questionId;
}