package com.htt.elearning.comment.service;

import com.htt.elearning.comment.dto.ReplyDTO;
import com.htt.elearning.comment.pojo.Comment;
import com.htt.elearning.comment.pojo.Replycomment;
import com.htt.elearning.comment.repository.CommentRepository;
import com.htt.elearning.comment.repository.ReplyCommentRepository;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.lesson.LessonClient;
import com.htt.elearning.user.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ReplyCommentServiceImpl implements ReplyCommentService {
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentRepository commentRepository;
    private final UserClient userClient;

    @Override
    public Replycomment createReplyComment(ReplyDTO replyDTO) throws DataNotFoundException {
        Comment existingComment = commentRepository.findById(replyDTO.getCommentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comment not found!!!"));

        Long userId = userClient.getUserIdByUsername();

        Replycomment newReplyComment = Replycomment.builder()
                .content(replyDTO.getContent())
                .comment(existingComment)
                .userId(userId)
                .createdDate(new Date())
                .build();

        replyCommentRepository.save(newReplyComment);

        return newReplyComment;
    }

    @Override
    public Page<Replycomment> getReplyByCommentId(Long commentId, Pageable pageable) throws DataNotFoundException {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comment not found!!"));

        return replyCommentRepository.findByCommentId(commentId, pageable)
                .map(Replycomment::fromReplyComment);
    }
}
