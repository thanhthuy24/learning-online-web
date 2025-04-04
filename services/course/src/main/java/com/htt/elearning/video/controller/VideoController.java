package com.htt.elearning.video.controller;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    public final VideoService videoService;

    @GetMapping("/count/lesson/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> getLessonCount(
            @PathVariable Long lessonId
    ) {
        return ResponseEntity.ok(videoService.countVideoByLessonId(lessonId));
    }

    @GetMapping("/lesson/{lessonId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getVideoByLessonId(
            @PathVariable Long lessonId
    ) throws DataNotFoundException {
        return ResponseEntity.ok(videoService.findByLessonId(lessonId));
    }

    @GetMapping("/count/course/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> countVideoByCourseId(
            @PathVariable Long courseId
    ){
        return ResponseEntity.ok(videoService.countVideoByCourseId(courseId));
    }

//    video - client
    @GetMapping("/count-lesson/{lessonId}")
    public Long getLessonCountClient(
            @PathVariable Long lessonId
    ) {
        return videoService.countVideoByLessonId(lessonId);
    }
}
