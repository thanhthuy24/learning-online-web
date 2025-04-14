package com.htt.elearning.comment.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.htt.elearning.comment.pojo.Comment;
import com.htt.elearning.lesson.response.LessonResponse;
import com.htt.elearning.user.response.UserResponse;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private Date createdDate;
    private LessonResponse lesson;
    private UserResponse user;

    public static CommentResponse fromComment(Comment comment, LessonResponse lesson, UserResponse user) {
        CommentResponse commentResponse = CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .lesson(lesson)
                .user(user)
                .build();
        return commentResponse;
    }
}
