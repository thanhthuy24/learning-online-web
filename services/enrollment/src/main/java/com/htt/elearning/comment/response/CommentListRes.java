package com.htt.elearning.comment.response;
import com.htt.elearning.comment.pojo.Comment;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentListRes {
    private List<CommentResponse> comments;
    private int totalPages;
}
