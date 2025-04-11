package com.htt.elearning.comment.service;

import com.htt.elearning.comment.dto.CommentDTO;
import com.htt.elearning.comment.pojo.Comment;
import com.htt.elearning.comment.repository.CommentRepository;
import com.htt.elearning.enrollment.pojo.Enrollment;
import com.htt.elearning.enrollment.repository.EnrollmentRepository;
import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.lesson.LessonClient;
import com.htt.elearning.lesson.response.LessonResponse;
import com.htt.elearning.sentiment.PerspectiveService;
//import com.htt.elearning.sentiment.SentimentService;
import com.htt.elearning.user.UserClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CommentRepository commentRepository;
    private final LessonClient lessonClient;
    private final UserClient userClient;
//    private final SentimentService sentimentService;
    private final PerspectiveService perspectiveService;
    private final HttpServletRequest request;

    @Override
    public Comment createComment(CommentDTO commentDTO) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

        LessonResponse existingLesson = lessonClient.getLessonById(commentDTO.getLessonId(), token);
        if (existingLesson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found");
        }

        Optional<Enrollment> existingEnrollment = enrollmentRepository
                .findByUserIdAndCourseId(userId, existingLesson.getCourseId());

        if (existingEnrollment == null || !existingEnrollment.isPresent()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This course isn't enrolled in your list! Please enroll before participating in this course!!");
        }

        Comment existingParentComment = commentRepository.getCommentById(commentDTO.getParentId());

        Double toxicityScore = perspectiveService.analyzeComment(commentDTO.getContent());
        String sentiment = perspectiveService.analyzeCommentSentiment(commentDTO.getContent());

        if (sentiment.equals("NEGATIVE") || toxicityScore < 0.5) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bình luận này bị chặn do có nội dung độc hại! 🔴");
        }

        Comment newComment = Comment.builder()
                .content(commentDTO.getContent())
                .createdDate(new Date())
                .userId(userId)
                .lessonId(existingLesson.getId())
                .sentiment(sentiment)
                .build();

        commentRepository.save(newComment);
        return newComment;
    }

//    @Override
//    public Page<CommentResponse> getCommentsByLessonId(Long lessonId, PageRequest pageRequest) throws DataNotFoundException {
//        Lesson existingLesson = lessonRepository.findById(lessonId)
//                .orElseThrow(() -> new DataNotFoundException("Lesson not found"));
//
//        Page<Comment> list = commentRepository.findByLessonId(lessonId, pageRequest);
//
//        return list.map(CommentResponse::fromComment);
//    }

    @Override
    public Page<Comment> getComments(Long lessonId, PageRequest pageRequest) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        LessonResponse existingLesson = lessonClient.getLessonById(lessonId, token);
        if (existingLesson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found");
        }
        return commentRepository.getCommentByLessonId(lessonId, pageRequest)
                .map(Comment::fromComment);
    }

    @Override
    public Comment createCommentChild(CommentDTO commentDTO, Long commentId) throws DataNotFoundException {
        String token = request.getHeader("Authorization");
        Long userId = userClient.getUserIdByUsername(token);

        LessonResponse existingLesson = lessonClient.getLessonById(commentDTO.getLessonId(), token);
        if (existingLesson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found");
        }

        Optional<Enrollment> existingEnrollment = enrollmentRepository
                .findByUserIdAndCourseId(userId, existingLesson.getCourseId());

        if (existingEnrollment == null || !existingEnrollment.isPresent()) {
            new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This course isn't enrolled in your list! Please enroll before participating in this course!!");
        }

        Comment newComment = Comment.builder()
                .content(commentDTO.getContent())
                .createdDate(new Date())
                .userId(userId)
                .lessonId(existingLesson.getId())
                .build();

        commentRepository.save(newComment);

        return newComment;
    }

    @Override
    public Long countCommentByLessonId(Long lessonId) {
        String token = request.getHeader("Authorization");
        LessonResponse existingLesson = lessonClient.getLessonById(lessonId, token);
        if (existingLesson == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found");
        }
        Long countCommentByLessonId = commentRepository.countCommentByLessonId(lessonId);
        return countCommentByLessonId;
    }
}
