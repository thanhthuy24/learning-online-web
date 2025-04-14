package com.htt.elearning.comment.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.htt.elearning.comment.pojo.Comment;
import com.htt.elearning.comment.pojo.Replycomment;
import com.htt.elearning.lesson.response.LessonResponse;
import com.htt.elearning.user.response.UserResponse;
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
    private Long commentId;
    private UserResponse user;

    public static ReplyCommentResponse fromReplyComment(Replycomment replycomment, UserResponse user) {
        ReplyCommentResponse commentResponse = ReplyCommentResponse.builder()
                .id(replycomment.getId())
                .content(replycomment.getContent())
                .createdDate(replycomment.getCreatedDate())
                .commentId(replycomment.getComment().getId())
                .user(user)
                .build();
        return commentResponse;
    }
}
