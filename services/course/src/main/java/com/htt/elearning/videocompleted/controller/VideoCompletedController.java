package com.htt.elearning.videocompleted.controller;

import com.htt.elearning.exceptions.DataNotFoundException;
import com.htt.elearning.videocompleted.dto.VideoCompletedDTO;
import com.htt.elearning.videocompleted.pojo.Videocompleted;
import com.htt.elearning.videocompleted.service.VideoCompletedService;
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
@RequestMapping("api/video-completed")
public class VideoCompletedController {
    private final VideoCompletedService videoCompleteService;

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getVideoCompleted(
            @PathVariable Long userId
    ) throws DataNotFoundException {
        List<Videocompleted> list = videoCompleteService.getVideoCompletedBy(userId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createVideoComplete(
            @RequestBody VideoCompletedDTO videoCompleteDTO,
            BindingResult rs
    ) throws DataNotFoundException {
        if(rs.hasErrors()){
            List<String> errorMessages = rs.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        videoCompleteService.createVideocompleted(videoCompleteDTO);
        return ResponseEntity.ok(videoCompleteDTO);
    }

//    VIDEO COMPLETED - CLIENT

    @GetMapping("/count-video-completed/{lessonId}")
    public Long countVideoCompletedBy
            (@PathVariable Long lessonId) throws DataNotFoundException
    {
        return videoCompleteService.countVideoCompletedBy(lessonId);
    }
}
