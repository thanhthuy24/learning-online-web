package com.htt.elearning.comment.response;

import com.htt.elearning.comment.pojo.Replycomment;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentListResponse {
    private List<ReplyCommentResponse> replycomments;
    private int totalPages;
}
