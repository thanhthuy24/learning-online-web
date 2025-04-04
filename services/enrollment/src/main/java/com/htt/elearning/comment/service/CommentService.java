package com.htt.elearning.comment.service;

import com.htt.elearning.comment.dto.CommentDTO;
import com.htt.elearning.comment.pojo.Comment;
import com.htt.elearning.exceptions.DataNotFoundException;
import org.springframework.data.domain.*;

public interface CommentService {
    Comment createComment(CommentDTO commentDTO) throws DataNotFoundException;
    Page<Comment> getComments(Long lessonId, PageRequest pageRequest) throws DataNotFoundException;
    Comment createCommentChild(CommentDTO commentDTO, Long commentId) throws DataNotFoundException;
    Long countCommentByLessonId(Long lessonId);
}
