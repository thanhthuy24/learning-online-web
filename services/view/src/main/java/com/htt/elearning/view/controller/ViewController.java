package com.htt.elearning.view.controller;

import com.htt.elearning.view.response.ViewResponse;
import com.htt.elearning.view.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/views")
public class ViewController {
    private final ViewService viewService;

    @PostMapping("/course/{courseId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ViewResponse createView(
            @PathVariable Long courseId
    ){
        return viewService.createView(courseId);
    }
}
