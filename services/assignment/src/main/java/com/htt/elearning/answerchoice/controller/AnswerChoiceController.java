package com.htt.elearning.answerchoice.controller;

import com.htt.elearning.answerchoice.dto.AnswerChoiceDTO;
import com.htt.elearning.answerchoice.repository.AnswerChoiceRepository;
import com.htt.elearning.answerchoice.response.AnswerChoiceResponse;

import com.htt.elearning.answerchoice.service.AnswerChoiceService;
import com.htt.elearning.exceptions.DataNotFoundException;
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
@RequestMapping("api/answer-choices")
public class AnswerChoiceController {
    private final AnswerChoiceService answerChoiceService;

    @PostMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAnswerChoice(
            @Valid
            @RequestBody List<AnswerChoiceDTO> answerChoiceDTOs,
            @PathVariable Long assignmentId,
            BindingResult rs
    ) throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        for (AnswerChoiceDTO answerChoiceDTO : answerChoiceDTOs) {
            answerChoiceService.createAnswerChoice(answerChoiceDTO, assignmentId);
        }

        return ResponseEntity.ok(answerChoiceDTOs);
    }

    @GetMapping("/assignment/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<AnswerChoiceResponse>> getAnswerChoiceByAssignment(
            @PathVariable Long assignmentId
    ) {
        List<AnswerChoiceResponse> answerchoiceList = answerChoiceService.checkAnswersByAssignmentId(assignmentId);
        return ResponseEntity.ok(answerchoiceList);
    }
}
