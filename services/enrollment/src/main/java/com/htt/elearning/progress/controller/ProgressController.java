package com.htt.elearning.progress.controller;

import com.htt.elearning.progress.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/progress")
public class ProgressController {
    private final ProgressService progressService;

    @PostMapping("/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Float createProgress(
            @PathVariable Long courseId
    ){
        return progressService.calculateProgress(courseId);
    }

    @GetMapping("/user/{userId}/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getProgress(
            @PathVariable Long userId,
            @PathVariable Long courseId
    ){
        return ResponseEntity.ok(progressService.getProgressByAdmin(userId, courseId));
    }

    @GetMapping("/get-progress/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getProgressByUser(
            @PathVariable Long courseId
    ){
        return ResponseEntity.ok(progressService.getProgressByUser(courseId));
    }

    @GetMapping("/check-progress/{courseId}/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> checkProgress(
            @PathVariable Long courseId,
            @PathVariable Long userId
    ){
        return ResponseEntity.ok(progressService.checkProgressForCertificate(userId, courseId));
    }
}
