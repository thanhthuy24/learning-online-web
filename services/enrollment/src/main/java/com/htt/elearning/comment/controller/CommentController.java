package com.htt.elearning.comment.controller;

import com.htt.elearning.comment.dto.CommentDTO;
import com.htt.elearning.comment.pojo.Comment;
import com.htt.elearning.comment.response.CommentListRes;
import com.htt.elearning.comment.service.CommentService;
import com.htt.elearning.exceptions.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createComment(
            @Valid
            @RequestBody CommentDTO commentDTO,
            BindingResult rs
    ) throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        commentService.createComment(commentDTO);
        return ResponseEntity.ok(commentDTO);
    }

//    @PostMapping("/{commentId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> createCommentChild(
//            @Valid
//            @RequestBody CommentDTO commentDTO,
//            @PathVariable Long commentId,
//            BindingResult rs
//    ) throws DataNotFoundException {
//        if(rs.hasErrors()){
//            List<String> errorMessages = rs.getFieldErrors()
//                    .stream()
//                    .map(FieldError::getDefaultMessage)
//                    .toList();
//            return ResponseEntity.badRequest().body(errorMessages);
//        }
//        commentService.createCommentChild(commentDTO, commentId);
//        return ResponseEntity.ok(commentDTO);
//    }

    @GetMapping("/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommentListRes> getComments(
            @PathVariable("lessonId") Long lessonId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) throws DataNotFoundException {
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("createdDate").descending());
        Page<Comment> list = commentService.getComments(lessonId, pageRequest);

        int totalPages = list.getTotalPages();

        List<Comment> cmts = list.getContent();
        return ResponseEntity.ok(CommentListRes.builder()
                .comments(cmts)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/count/lesson/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> countCommentsByLessonId(
            @PathVariable("lessonId") Long lessonId
    ){
        return ResponseEntity.ok(commentService.countCommentByLessonId(lessonId));
    }
}
