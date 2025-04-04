package com.htt.elearning.comment.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentListResponse {
    private List<CommentResponse> comments;
    private int totalPages;
}
