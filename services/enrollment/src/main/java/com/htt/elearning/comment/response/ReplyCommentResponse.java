package com.htt.elearning.comment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyCommentResponse {
    private Long id;
    private String content;
    private Date createdDate;
    @JsonProperty("comment_id")
    private Long commentId;
    @JsonProperty("user_id")
    private Long userId;
}
