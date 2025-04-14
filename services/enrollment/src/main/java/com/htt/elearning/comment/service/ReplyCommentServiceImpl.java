package com.htt.elearning.comment.service;

import com.htt.elearning.comment.dto.ReplyDTO;
import com.htt.elearning.comment.pojo.Comment;
import com.htt.elearning.comment.pojo.Replycomment;
import com.htt.elearning.comment.repository.CommentRepository;
import com.htt.elearning.comment.repository.ReplyCommentRepository;
import com.htt.elearning.comment.response.CommentResponse;
import com.htt.elearning.comment.response.ReplyCommentResponse;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.lesson.LessonClient;
import com.htt.elearning.user.UserClient;
import com.htt.elearning.user.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyCommentServiceImpl implements ReplyCommentService {
    private final ReplyCommentRepository replyCommentRepository;
    private final CommentRepository commentRepository;
    private final UserClient userClient;
    private final HttpServletRequest request;

    @Override
    public Replycomment createReplyComment(ReplyDTO replyDTO) throws DataNotFoundException {
        Comment existingComment = commentRepository.findById(replyDTO.getCommentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comment not found!!!"));

        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

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
    public Page<ReplyCommentResponse> getReplyByCommentId(Long commentId, PageRequest pageRequest) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comment not found!!"));

        Page<Replycomment> replycommentPage = replyCommentRepository.findByCommentId(commentId, pageRequest);
        List<Replycomment> replyComments = replycommentPage.getContent();

        List<Long> userIds = replyComments.stream()
                .map(Replycomment::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<UserResponse> users = userClient.getUsersByIdsClient(userIds, token);
        Map<Long, UserResponse> usersMap = users.stream()
                .collect(Collectors.toMap(UserResponse::getId, Function.identity()));

        List<ReplyCommentResponse> replyCommentResponses = replyComments.stream()
                .map(comment -> {
                    UserResponse user = usersMap.get(comment.getUserId());
                    return ReplyCommentResponse.fromReplyComment(comment, user);
                })
                .collect(Collectors.toList());
        return new PageImpl<>(replyCommentResponses, pageRequest, replycommentPage.getTotalElements());
    }
}
