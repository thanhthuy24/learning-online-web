package com.htt.elearning.interactions.controller;


import com.htt.elearning.interactions.dto.InteractionDTO;
import com.htt.elearning.interactions.pojo.Interaction;
import com.htt.elearning.interactions.response.InteractionResponse;
import com.htt.elearning.interactions.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interactions")
public class InteractionController {
    private final InteractionService interactionService;

    @PostMapping("/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InteractionResponse> createInteraction(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(interactionService.createInteraction(courseId));
    }

    @PutMapping("/update/{interactionId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InteractionResponse> updateInteraction(
            @PathVariable Long interactionId
    ) {
        return ResponseEntity.ok(interactionService.updateInteraction(interactionId));
    }

    @GetMapping("/get-interaction-by-courseId/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InteractionResponse> getInteractionByCourseId(
            @PathVariable Long courseId
    ){
        return ResponseEntity.ok(interactionService.getInteraction(courseId));
    }
}
