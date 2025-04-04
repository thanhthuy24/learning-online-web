package com.htt.elearning.comment.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String content;
    @JsonProperty("created_date")
    private Date createdDate;
    @JsonProperty("lesson_id")
    private Long lessonId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("parent_id")
    private Long parentId;
}
