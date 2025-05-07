package com.htt.elearning.interaction;

import com.htt.elearning.interaction.dto.InteractionDTO;
import com.htt.elearning.interaction.response.InteractionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "enrollment-service-v2",
        url = "${application.config.interactions-url}"
)
public interface InteractionClient {
    @PostMapping("/courseId")
    ResponseEntity<InteractionResponse> createInteraction(
            @PathVariable Long courseId,
            @RequestHeader("Authorization") String token
    );

    @PutMapping("/update/{interactionId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InteractionResponse> updateInteraction(
            @PathVariable Long interactionId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/get-interaction-by-courseId/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InteractionResponse> getInteractionByCourseId(
            @PathVariable Long courseId,
            @RequestHeader("Authorization") String token
    );
}
