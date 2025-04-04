package com.htt.elearning.rating.controller;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.rating.dto.CourseRatingDTO;
import com.htt.elearning.rating.pojo.Courserating;
import com.htt.elearning.rating.response.RatingListResponse;
import com.htt.elearning.rating.service.CourseRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/rating")
@RequiredArgsConstructor
public class CourseRatingController {
    private final CourseRatingService courseRatingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createRating(
            @Valid
            @RequestBody CourseRatingDTO courseRatingDTO,
            BindingResult rs
    ) throws DataNotFoundException, JSONException {
        if (rs.hasErrors()) {
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        courseRatingService.createRating(courseRatingDTO);
        return ResponseEntity.ok(courseRatingDTO);
    }

    @GetMapping("/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RatingListResponse> getCourseRating(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @PathVariable Long courseId
    ) throws DataNotFoundException {
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("ratingDate").descending());

        Page<Courserating> courseratingPage = courseRatingService.getRatingByCourseId(courseId, pageRequest);

        int totalPages = courseratingPage.getTotalPages();

        List<Courserating> courseratings = courseratingPage.getContent();
        return ResponseEntity.ok(RatingListResponse.builder()
                .ratings(courseratings)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/find-by-sentiment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RatingListResponse> findBySentiment(
            @RequestParam(required = false) String sentiment,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(required = false) Long courseId
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("ratingDate").descending());

        Page<Courserating> courseratingPage = courseRatingService.getRatingsBySentiment(courseId, sentiment, pageRequest);

        int totalPages = courseratingPage.getTotalPages();

        List<Courserating> courseratings = courseratingPage.getContent();
        return ResponseEntity.ok(RatingListResponse.builder()
                .ratings(courseratings)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/find-by-rate")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RatingListResponse> findByRate(
            @RequestParam(required = false) Long rate,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam(required = false) Long courseId
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("ratingDate").descending());

        Page<Courserating> courseratingPage = courseRatingService.getRatingsByRate(courseId,rate, pageRequest);

        int totalPages = courseratingPage.getTotalPages();

        List<Courserating> courseratings = courseratingPage.getContent();
        return ResponseEntity.ok(RatingListResponse.builder()
                .ratings(courseratings)
                .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> averageRating(
            @PathVariable Long courseId
    ) throws DataNotFoundException {
        return ResponseEntity.ok(courseRatingService.averageRatingByCourseId(courseId));
    }

    @GetMapping("/{courseId}/rating/{rating}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> averageRatingByStar(
            @PathVariable Long courseId,
            @PathVariable Long rating
    ) throws DataNotFoundException {
        return ResponseEntity.ok(courseRatingService.averageRatingByStar(rating, courseId));
    }

    @GetMapping("/{courseId}/rate/{rating}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> countRatingByCourseIdAndRating(
            @PathVariable Long courseId,
            @PathVariable Long rating
    ) throws DataNotFoundException {
        return ResponseEntity.ok(courseRatingService.countRatingByCourseIdByRating(courseId, rating));
    }

    @GetMapping("/{courseId}/count")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> countRatingByCourseId(
            @PathVariable Long courseId
    ) throws DataNotFoundException {
        return ResponseEntity.ok(courseRatingService.countAll(courseId));
    }
}
