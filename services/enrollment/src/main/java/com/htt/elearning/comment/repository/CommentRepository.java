package com.htt.elearning.comment.repository;

import com.htt.elearning.comment.pojo.Comment;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByLessonId(Long lessonId, PageRequest pageRequest);
    Comment getCommentById(Long id);
    Page<Comment> getCommentByLessonId(Long lessonId, PageRequest pageRequest);
    Long countCommentByLessonId(Long lessonId);
}
