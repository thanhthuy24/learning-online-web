package com.htt.elearning.course.controller;

import com.htt.elearning.course.dto.CourseDTO;
import com.htt.elearning.course.dto.RecommendDTO;
import com.htt.elearning.course.pojo.Course;
import com.htt.elearning.course.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/ai-recommend")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRecommendation(
            @PathVariable Long userId
    ) {
        List<CourseDTO> recommendCourses = recommendationService.getRecommendedCourses(userId);
        return ResponseEntity.ok(recommendCourses);
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRecommendation2(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(recommendationService.getRecommendedCourses2(userId));
    }

    @PostMapping("/new-user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getRecommendationForNewUser(
        @RequestBody RecommendDTO recommendDTO
    ) {
        return ResponseEntity.ok(recommendationService.getRecommendedCoursesForNewUser(recommendDTO));
    }
}
