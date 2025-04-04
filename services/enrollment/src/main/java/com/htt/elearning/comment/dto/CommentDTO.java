package com.htt.elearning.comment.dto;

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
public class CommentDTO {
    @NotNull
    @Size(min = 2, max = 4000, message = "Comment must be between 2 and 200 characters")
    private String content;
    @JsonProperty("lesson_id")
    private Long lessonId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("parent_id")
    private Long parentId;
}
