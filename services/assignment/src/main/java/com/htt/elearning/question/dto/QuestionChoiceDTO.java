package com.htt.elearning.question.dto;

import com.htt.elearning.choice.pojo.Choice;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionChoiceDTO {
    private Long id;
    private String content;
    private List<Choice> choices;
}
