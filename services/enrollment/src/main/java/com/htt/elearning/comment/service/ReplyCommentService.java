package com.htt.elearning.comment.service;

import com.htt.elearning.comment.dto.ReplyDTO;
import com.htt.elearning.comment.pojo.Replycomment;
import com.htt.elearning.exceptions.DataNotFoundException;
import org.springframework.data.domain.*;

public interface ReplyCommentService {
    Replycomment createReplyComment(ReplyDTO replyDTO) throws DataNotFoundException;
    Page<Replycomment> getReplyByCommentId(Long commentId, Pageable pageable) throws DataNotFoundException;
}
