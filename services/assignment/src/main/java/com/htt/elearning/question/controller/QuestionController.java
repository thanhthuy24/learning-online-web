package com.htt.elearning.question.controller;

import com.htt.elearning.question.dto.QuestionChoiceDTO;
import com.htt.elearning.question.dto.QuestionDTO;
import com.htt.elearning.question.dto.QuestionEssayDTO;
import com.htt.elearning.question.pojo.Question;
import com.htt.elearning.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("api/questions")
public class QuestionController{
    private final QuestionService questionService;

    @GetMapping("/assignment/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<QuestionChoiceDTO>> getQuestionByAssignmentId(
            @PathVariable(value = "assignmentId") Long assignmentId
    ){
        List<QuestionChoiceDTO> questionChoiceDTOList = questionService.getQuestionsByAssignmentId(assignmentId);
        return ResponseEntity.ok(questionChoiceDTOList);
    }

    @GetMapping("/essay/assignment/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<QuestionEssayDTO>> getQuestionEssayByAssignmentId(
            @PathVariable(value = "assignmentId") Long assignmentId
    ){
        List<QuestionEssayDTO> QuestionEssayDTOList = questionService.getQuestionEssaysByAssignmentId(assignmentId);
        return ResponseEntity.ok(QuestionEssayDTOList);
    }

    @GetMapping("/count/assignment/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> countQuestionByAssignmentId(
            @PathVariable(value = "assignmentId") Long assignmentId
    ){
        return ResponseEntity.ok(questionService.countQuestionByAssignmentId(assignmentId));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createQuestion(
            @RequestBody QuestionDTO questionDTO,
            BindingResult rs
    ){
        try {
            if (rs.hasErrors()) {
                List<String> errorMessages = rs.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Question question = questionService.createQuestion(questionDTO);

            return ResponseEntity.ok(question);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateQuestion(
            @PathVariable(value = "questionId") Long questionId,
            @Valid
            @RequestBody QuestionDTO questionDTO
    ){
        questionService.updateQuestion(questionId, questionDTO);

        return ResponseEntity.ok(questionDTO);
    }
}
