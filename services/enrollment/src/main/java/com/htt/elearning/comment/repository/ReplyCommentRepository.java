package com.htt.elearning.comment.repository;

import com.htt.elearning.comment.pojo.Replycomment;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyCommentRepository extends JpaRepository<Replycomment, Long> {
    Page<Replycomment> findByCommentId(Long commentId, Pageable pageable);
}
