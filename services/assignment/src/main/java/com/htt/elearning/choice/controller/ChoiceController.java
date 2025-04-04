package com.htt.elearning.choice.controller;

import com.htt.elearning.choice.dto.ChoiceDTO;
import com.htt.elearning.choice.service.ChoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("api/choices")
public class ChoiceController {
    private final ChoiceService choiceService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createChoice(
            @Valid
            @RequestBody List<ChoiceDTO> choiceDTOs,
            BindingResult rs
    ){
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        for (ChoiceDTO choiceDTO : choiceDTOs) {
            choiceService.createChoice(choiceDTO);
        }

        return ResponseEntity.ok(choiceDTOs);
    }

    @PutMapping("/{choiceId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateChoice(
            @Valid
            @PathVariable(value = "choiceId") Long choiceId,
            @RequestBody ChoiceDTO choiceDTO
    ){
        choiceService.updateChoice(choiceId, choiceDTO);
        return ResponseEntity.ok(choiceDTO);
    }
}
