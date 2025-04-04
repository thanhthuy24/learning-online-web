package com.htt.elearning.score.controller;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.score.dto.ScoreDTO;
import com.htt.elearning.score.service.ScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/score")
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createScore(
            @Valid
            @RequestBody ScoreDTO scoreDTO,
            BindingResult rs
    ) throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        scoreService.createScore(scoreDTO);
        return ResponseEntity.ok(scoreDTO);
    }

    @PostMapping("/essay/{essayId}")
    public ResponseEntity<?> createScoreEssay(
            @Valid
            @RequestBody ScoreDTO scoreDTO,
            @PathVariable Long essayId,
            BindingResult rs
    ) throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        scoreService.createScoreEssay(scoreDTO, essayId);
        return ResponseEntity.ok(scoreDTO);
    }

    @GetMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getScoreByAssignmentId(
            @PathVariable Long assignmentId
    ) throws DataNotFoundException {
        return ResponseEntity.ok(scoreService.getScoreByAssignmentId(assignmentId));
    }

    @GetMapping("/get-score/assignment/{assignmentId}/by-user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getScoreByAssignmentIdAndUserId(
            @PathVariable Long assignmentId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(scoreService.getScoreByAssignmentIdAndUserId(assignmentId, userId));
    }

}
