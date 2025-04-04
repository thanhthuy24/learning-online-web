package com.htt.elearning.question.dto;

import com.htt.elearning.essay.pojo.Essay;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionEssayDTO {
    private Long id;
    private String content;
    private List<Essay> essays;
}
